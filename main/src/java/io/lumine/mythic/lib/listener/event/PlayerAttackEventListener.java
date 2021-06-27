package io.lumine.mythic.lib.listener.event;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.AttackResult;
import io.lumine.mythic.lib.api.DamageType;
import io.lumine.mythic.lib.api.RegisteredAttack;
import io.lumine.mythic.lib.api.event.EntityKillEntityEvent;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * First problem: you want to create a skill which does something whenever
 * a player attacks a given entity, but you have to listen to all the attack
 * types: ranged attacks with bows, melee attacks with sword... and even abilities.
 * We need some event to generalize every attack, which is {@link PlayerAttackEvent}
 * <p>
 * A PlayerAttackEvent is called WHENEVER a player attacks, by any way, an entity.
 * This is super useful for MMOCore skills and other external plugins.
 * <p>
 * Second problem: if a player shoots another entity, it's not hard to get the
 * damaging entity, the arrow and trace back its shooter. However, if an external
 * plugin damages an entity WITHOUT telling Spigot that the player is the damage
 * source, it's IMPOSSIBLE to trace back the initial damager.
 * <p>
 * DamageManager gives a way to let MythicLib know that some player damaged some
 * entity. Basically MythicLib is monitoring every single attack from every single
 * player to keep track 1) of the initial caster, 2) of the damage types, that
 * is whether it is a SKILL or WEAPON attack.
 *
 * @author indyuce
 */
public class PlayerAttackEventListener implements Listener {

    /**
     * Calls a PlayerAttackEvent whenever an entity is attacked, only if
     * MythicLib manages to find
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void registerEvents(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Damageable))
            return;

        /**
         * Looks for a RegisteredAttack. If it 1) can't find one registered in the {@link DamageManager}
         * and if MythicLib 2) cannot generate one specifically for this damage event, the
         * MythicLib will NOT monitor this attack i.e NEITHER apply damage stats NOR call PlayerAttackEvent.
         *
         * At the moment, MythicLib does not monitor attacks with no damager either.
         */
        RegisteredAttack attack = findAttack(event);
        if (attack == null || attack.getDamager() == null)
            return;

        /**
         * If the damager is a player, call {@link PlayerAttackEvent}
         */
        if (attack.getDamager() instanceof Player && !attack.getDamager().hasMetadata("NPC")) {

            PlayerAttackEvent attackEvent = new PlayerAttackEvent(MMOPlayerData.get((Player) attack.getDamager()), event, attack.getResult());
            Bukkit.getPluginManager().callEvent(attackEvent);
            if (attackEvent.isCancelled())
                return;

            event.setDamage(attack.getResult().getDamage());
        }

        /**
         * If the entity is killed, call {@link EntityKillEntityEvent}
         */
        if (event.getFinalDamage() >= ((Damageable) event.getEntity()).getHealth())
            Bukkit.getPluginManager().callEvent(new EntityKillEntityEvent(attack.getDamager(), event.getEntity()));
    }

    /**
     * Very important method. Looks for a RegisteredAttack that would have been registered
     * by other plugins ie MMOItems abilities, or MythicCore abilities, or any other plugin.
     * <p>
     * If it can't find any plugin that has registered an attack, it checks if it is simply
     * not just a melee attack (then a
     */
    private RegisteredAttack findAttack(EntityDamageByEntityEvent event) {

        /**
         * Checks in the MythicLib registered attack. This is used by MMOItems skills,
         * MMOCore skills, or any other plugin that implement MythicLib compatibility.
         */
        RegisteredAttack custom = MythicLib.plugin.getDamage().findInfo(event.getEntity());
        if (custom != null) {
            custom.getResult().setDamage(event.getDamage());
            return custom;
        }

        /**
         * Handles melee attacks. This is used everytime a player left clicks an entity.
         *
         * The attack damage type can vary depending on the context: if it is a bare-firsts
         * attack, final attack has no WEAPON damage type. If the player is holding any
         * other item, it is considered a WEAPON attack.
         */
        if (event.getDamager() instanceof LivingEntity)
            return new RegisteredAttack(new AttackResult(event.getDamage(), getDamageTypes(event)), (LivingEntity) event.getDamager());

        /**
         * Handles projectile attacks; used everytime when a player shoots a trident,
         * a bow, a crossbow or even eggs and snowballs.
         *
         * Notice this is always the same damage type: WEAPON, PHYSICAL, PROJECTILE
         * which means that if MMOCore has a skill which makes players shoot multiple
         * arrows, MythicLib will use the following lines to monitor the attacks
         * and the skill will apply WEAPON damage.
         */
        if (event.getDamager() instanceof Projectile) {
            Projectile proj = (Projectile) event.getDamager();
            if (proj.getShooter() instanceof LivingEntity)
                return new RegisteredAttack(new AttackResult(event.getDamage(), DamageType.WEAPON, DamageType.PHYSICAL, DamageType.PROJECTILE),
                        (LivingEntity) proj.getShooter());
        }

        return null;
    }

    /**
     * @param  event The attack event
     * @return       The damage types of a vanilla melee entity attack
     */
    private DamageType[] getDamageTypes(EntityDamageByEntityEvent event) {

        /**
         * If the attacker has no item in his hand when attacking, attack is
         * only physical
         */
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity) event.getDamager();
            if (isAir(damager.getEquipment().getItemInMainHand()))
                return new DamageType[] { DamageType.PHYSICAL };
        }

        /**
         * By default a physical attack is a weapon-physical attack
         */
        return new DamageType[] { DamageType.WEAPON, DamageType.PHYSICAL };
    }

    private boolean isAir(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
}

