package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;

public class DamageManager implements Listener, AttackHandler {
    private final Map<Integer, AttackMetadata> customDamage = new HashMap<>();
    private final Set<AttackHandler> handlers = new HashSet<>();

    private static final AttributeModifier NO_KNOCKBACK = new AttributeModifier(UUID.randomUUID(), "noKnockback", 100, AttributeModifier.Operation.ADD_NUMBER);
    private static final double MINIMUM_DAMAGE = .001;

    public DamageManager() {
        handlers.add(this);
    }

    /**
     * Damage handlers are used by MythicLib to keep track of details of every
     * attack so that it can apply damage based stats like PvE damage, Magic
     * Damage...
     *
     * @param handler Damage handler being registered
     */
    public void registerHandler(AttackHandler handler) {
        Validate.notNull(handler, "Damage handler cannot be null");

        handlers.add(handler);
    }

    @Override
    @Nullable
    public AttackMetadata getAttack(EntityDamageEvent event) {
        return customDamage.get(event.getEntity().getEntityId());
    }

    /**
     * Forces a player to damage an entity with knockback
     *
     * @param player The player damaging the entity
     * @param target The entity being damaged
     * @param result Info about the attack. Since this attack is registered as
     *               MythicLib damage, we need more than just a double for the atk
     *               damage
     */
    @Deprecated
    public void damage(Player player, LivingEntity target, DamageMetadata result) {
        damage(player, target, result, true);
    }

    /**
     * Forces a player to damage an entity with or without knockback
     *
     * @param player    The player damaging the entity
     * @param target    The entity being damaged
     * @param result    Info about the attack. Since this attack is registered as
     *                  MythicLib damage, we need more than just a double for the atk
     *                  damage
     * @param knockback If the attack should inflict knockback
     */
    @Deprecated
    public void damage(@NotNull Player player, @NotNull LivingEntity target, @NotNull DamageMetadata result, boolean knockback) {
        AttackMetadata metadata = new AttackMetadata(result, MMOPlayerData.get(player).getStatMap().cache(EquipmentSlot.MAIN_HAND));
        damage(metadata, target, true);
    }

    /**
     * Forces a player to damage an entity with knockback
     *
     * @param metadata The class containing all info about the current attack
     * @param target   The entity being damaged
     */
    public void damage(@NotNull AttackMetadata metadata, @NotNull LivingEntity target) {
        damage(metadata, target, true);
    }

    /**
     * Forces a player to damage an entity with (no) knockback
     *
     * @param metadata  The class containing all info about the current attack
     * @param target    The entity being damaged
     * @param knockback If the attack should deal knockback
     */
    public void damage(@NotNull AttackMetadata metadata, @NotNull LivingEntity target, boolean knockback) {
        damage(metadata, target, knockback, false);
    }

    /**
     * Forces a player to damage an entity.
     *
     * @param metadata       The class containing all info about the current attack
     * @param target         The entity being damaged
     * @param knockback      If the attack should deal knockback
     * @param ignoreImmunity The attack will not produce immunity frames.
     */
    public void damage(@NotNull AttackMetadata metadata, @NotNull LivingEntity target, boolean knockback, boolean ignoreImmunity) {

        // TODO remove this check which should be useless
        if (target.hasMetadata("NPC") || metadata.getPlayer().hasMetadata("NPC"))
            return;

        customDamage.put(target.getEntityId(), metadata);
        applyDamage(Math.max(metadata.getDamage().getDamage(), MINIMUM_DAMAGE), target, metadata.getPlayer(), knockback, ignoreImmunity);
    }

    private void applyDamage(double damage, LivingEntity target, Player damager, boolean knockback, boolean ignoreImmunity) {

        // Should knockback be applied
        if (!knockback) {
            AttributeInstance instance = target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            try {
                instance.addModifier(NO_KNOCKBACK);
                applyDamage(damage, target, damager, true, ignoreImmunity);
            } catch (Exception anyError) {
                MythicLib.plugin.getLogger().log(Level.SEVERE, "An error occured while registering player damage");
                anyError.printStackTrace();
            } finally {
                instance.removeModifier(NO_KNOCKBACK);
            }

            // Should damage immunity be taken into account
        } else if (ignoreImmunity) {
            int noDamageTicks = target.getNoDamageTicks();
            try {
                target.setNoDamageTicks(0);
                applyDamage(damage, target, damager, true, false);
            } catch (Exception anyError) {
                MythicLib.plugin.getLogger().log(Level.SEVERE, "An error occured while registering player damage");
                anyError.printStackTrace();
            } finally {
                target.setNoDamageTicks(noDamageTicks);
            }

            // Just damage entity
        } else
            target.damage(damage, damager);
    }

    /**
     * This method is used to unregister MythicLib custom damage after everything
     * was calculated, hence MONITOR priority
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void unregisterCustomDamage(EntityDamageByEntityEvent event) {
        customDamage.remove(event.getEntity().getEntityId());
    }

    /**
     * This method draws an interface between MythicLib damage mitigation system
     * and Bukkit damage events.
     * <p>
     * Unlike {@link #getAttack(EntityDamageEvent)} which can return a null object
     * if MythicLib cannot find an attack source, this method NEVER returns null. In
     * the worst case (unknown damage cause/damage not logged by any plugin) scenario,
     * it just returns a damage metadata with no damage type which is completely fine.
     *
     * @param event The damage event
     * @return The corresponding MythicLib damage metadata.
     */
    @NotNull
    public DamageMetadata findDamage(EntityDamageEvent event) {

        // Handle damage from other plugins
        if (event instanceof EntityDamageByEntityEvent) {
            AttackMetadata attackMeta = findAttack((EntityDamageByEntityEvent) event);
            if (attackMeta != null)
                return attackMeta.getDamage();
        }

        // Handle vanilla damage
        switch (event.getCause()) {
            case MAGIC:
            case DRAGON_BREATH:
                return new DamageMetadata(event.getDamage(), DamageType.MAGIC);
            case POISON:
            case WITHER:
                return new DamageMetadata(event.getDamage(), DamageType.MAGIC, DamageType.DOT);
            case FIRE_TICK:
            case MELTING:
                return new DamageMetadata(event.getDamage(), DamageType.PHYSICAL, DamageType.DOT);
            case FALL:
            case THORNS:
            case CONTACT:
            case ENTITY_EXPLOSION:
            case ENTITY_SWEEP_ATTACK:
            case FALLING_BLOCK:
            case FLY_INTO_WALL:
            case BLOCK_EXPLOSION:
            case ENTITY_ATTACK:
                return new DamageMetadata(event.getDamage(), DamageType.PHYSICAL);
            case PROJECTILE:
                return new DamageMetadata(event.getDamage(), DamageType.PHYSICAL, DamageType.PROJECTILE);
            default:
                return new DamageMetadata(event.getDamage());
        }
    }

    /**
     * Very important method. Looks for a RegisteredAttack that would have been registered
     * by other plugins ie MMOItems abilities, or MMOCore skills, or any other plugin.
     * <p>
     * If it can't find any plugin that has registered an attack, it checks if it is simply
     * not just a vanilla attack: projectile or melee attacks.
     *
     * @param event The attack event
     * @return Null if MythicLib cannot find the attack source, some attack meta otherwise.
     */
    @Nullable
    public AttackMetadata findAttack(EntityDamageByEntityEvent event) {

        /*
         * Checks in the MythicLib registered attack. This is used by MMOItems skills,
         * MMOCore skills, or any other plugin that implement MythicLib compatibility.
         */
        for (AttackHandler handler : handlers) {
            AttackMetadata found = handler.getAttack(event);
            if (found != null)
                return found;
        }

        // Players damaging Citizens NPCs are not registered
        if (event.getEntity().hasMetadata("NPC"))
            return null;

        /*
         * Handles melee attacks. This is used everytime a player left clicks an entity.
         *
         * The attack damage type can vary depending on the context: if it is a bare-firsts
         * attack, final attack has no WEAPON damage type. If the player is holding any
         * other item, it is considered a WEAPON attack.
         */
        if (isRealPlayer(event.getDamager()))
            return new MeleeAttackMetadata(new DamageMetadata(event.getDamage(), getDamageTypes(event)), MMOPlayerData.get((Player) event.getDamager()).getStatMap().cache(EquipmentSlot.MAIN_HAND));

        /*
         * Handles projectile attacks; used everytime when a player shoots a trident,
         * a bow, a crossbow or even eggs and snowballs.
         *
         * Notice this is always the same damage type: WEAPON, PHYSICAL, PROJECTILE
         * which means that if MMOCore has a skill which makes players shoot multiple
         * arrows, MythicLib will use the following lines to monitor the attacks
         * and the skill will apply WEAPON damage.
         *
         * Make sure to check the shooter is not the damaged entity. We don't want
         * players to backstab themselves using projectiles.
         */
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            ProjectileSource source = projectile.getShooter();
            if (source != null && !source.equals(event.getEntity()) && isRealPlayer(source))
                return new ProjectileAttackMetadata(new DamageMetadata(event.getDamage(), DamageType.WEAPON, DamageType.PHYSICAL, DamageType.PROJECTILE),
                        MMOPlayerData.get((Player) source).getStatMap().cache(EquipmentSlot.MAIN_HAND), projectile);
        }

        return null;
    }

    /**
     * @return If the entity is a player and NOT a Citizens or Sentinels NPC
     */
    private boolean isRealPlayer(Object entity) {
        return entity instanceof Player && !((Player) entity).hasMetadata("NPC");
    }

    /**
     * @param event The attack event
     * @return The damage types of a vanilla melee entity attack
     */
    private DamageType[] getDamageTypes(EntityDamageByEntityEvent event) {
        Validate.isTrue(event.getDamager() instanceof LivingEntity, "Not an entity attack");

        // Physical attack with bare fists.
        LivingEntity damager = (LivingEntity) event.getDamager();
        if (isAir(damager.getEquipment().getItemInMainHand()))
            return new DamageType[]{DamageType.UNARMED, DamageType.PHYSICAL};

        // By default a physical attack is a weapon-physical attack
        return new DamageType[]{DamageType.WEAPON, DamageType.PHYSICAL};
    }

    private boolean isAir(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
}
