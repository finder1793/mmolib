package io.lumine.mythic.lib.listener.event;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.event.PlayerKillEntityEvent;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.*;
import org.apache.commons.lang.Validate;
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
import org.bukkit.projectiles.ProjectileSource;

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
     * MythicLib manages to find an attacker.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void registerEvents(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Damageable) || event.getDamage() == 0)
            return;

        /*
         * Looks for a RegisteredAttack. If it 1) can't find one registered in the {@link DamageManager}
         * and if MythicLib 2) cannot generate one specifically for this damage event, the
         * MythicLib will NOT monitor this attack i.e NEITHER apply damage stats NOR call PlayerAttackEvent.
         */
        AttackMetadata attack = MythicLib.plugin.getDamage().findAttack(event);
        if (attack == null)
            return;

        // Call the Bukkit event with the attack meta found
        Validate.isTrue(!attack.hasExpired(), "Attack has already expired");
        PlayerAttackEvent attackEvent = new PlayerAttackEvent(event, attack);
        Bukkit.getPluginManager().callEvent(attackEvent);
        attack.expire();
        if (attackEvent.isCancelled())
            return;

        event.setDamage(attack.getDamage().getDamage());

        // Call the death event if the entity is being killed
        if (event.getFinalDamage() >= ((Damageable) event.getEntity()).getHealth())
            Bukkit.getPluginManager().callEvent(new PlayerKillEntityEvent(attack, (LivingEntity) event.getEntity()));
    }
}

