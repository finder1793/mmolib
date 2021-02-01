package io.lumine.mythic.lib.listener.event;

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

public class PlayerAttackEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void registerEvents(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Damageable))
            return;

        /*
         * Looks for a registered attack from MMOLib damage system, or
         * initializes an attack based either on a melee or projectile vanilla
         * attack
         */
        RegisteredAttack attack = findAttack(event);

        /*
         * If the damager cannot be found, no PlayerAttackEvent should be called
         */
        if (attack == null || attack.getDamager() == null)
            return;

        /*
         * If the damager is a player, register a custom event used in MMOCore
         * to easily handle on hit skills for example. Can also be used by
         * external plugins
         */
        if (attack.getDamager() instanceof Player && !attack.getDamager().hasMetadata("NPC")) {

            PlayerAttackEvent attackEvent = new PlayerAttackEvent(MMOPlayerData.get((Player) attack.getDamager()), event, attack.getResult());
            Bukkit.getPluginManager().callEvent(attackEvent);
            if (attackEvent.isCancelled())
                return;

            event.setDamage(attack.getResult().getDamage());
        }

        /*
         * Checks for killing
         */
        if (event.getFinalDamage() >= ((Damageable) event.getEntity()).getHealth())
            Bukkit.getPluginManager().callEvent(new EntityKillEntityEvent(attack.getDamager(), event.getEntity()));
    }

    private RegisteredAttack findAttack(EntityDamageByEntityEvent event) {

        /*
         * Check MMOLib registered attacks database and updates damage dealt
         * based on the value given by the Bukkit event
         */
        RegisteredAttack custom = MythicLib.plugin.getDamage().findInfo(event.getEntity());
        if (custom != null) {
            custom.getResult().setDamage(event.getDamage());
            return custom;
        }

        /*
         * Check direct damager
         */
        if (event.getDamager() instanceof LivingEntity)
            return new RegisteredAttack(new AttackResult(event.getDamage(), getDamageTypes(event)), (LivingEntity) event.getDamager());

        /*
         * Checks projectile and add damage type, which supports every vanilla
         * projectile like snowballs, tridents and arrows
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

        /*
         * If the attacker has no item in his hand when attacking, attack is
         * only physical
         */
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity) event.getDamager();
            if (isAir(damager.getEquipment().getItemInMainHand()))
                return new DamageType[] { DamageType.PHYSICAL };
        }

        /*
         * By default a physical attack is a weapon-physical attack
         */
        return new DamageType[] { DamageType.WEAPON, DamageType.PHYSICAL };
    }

    private boolean isAir(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
}

