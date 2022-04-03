package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.AttackHandler;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import org.apache.commons.lang.Validate;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
     * @param event Damage event.
     * @return Null if the entity is being damaged through vanilla
     *         actions, or the corresponding RegisteredAttack if MythicLib
     *         found a plugin responsible for that damage
     */
    @Nullable
    public AttackMetadata findInfo(EntityDamageEvent event) {
        for (AttackHandler handler : handlers) {
            AttackMetadata found = handler.getAttack(event);
            if (found != null)
                return found;
        }
        return null;
    }

    /**
     * This method is used to unregister MythicLib custom damage after everything
     * was calculated, hence MONITOR priority
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void unregisterCustomDamage(EntityDamageByEntityEvent event) {
        customDamage.remove(event.getEntity().getEntityId());
    }
}
