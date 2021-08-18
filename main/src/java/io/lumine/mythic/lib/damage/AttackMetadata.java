package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.stat.StatMap;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Instanced every time MythicLib detects and monitors one attack from one player.
 *
 * @author Indyuce
 */
public class AttackMetadata {
    private final Player player;
    private final StatMap.CachedStatMap statMap;
    private final DamageMetadata damage;

    private boolean successful = true;

    /**
     * Used by DamageHandler instances to register attacks. AttackResult only
     * gives information about the attack damage and types while this class also
     * contains info about the damager. Some plugins don't let MMOLib determine
     * what the damager is so there might be problem with damage/reduction stat
     * application.
     *
     * @param damage  The attack result
     * @param statMap The entity who dealt the damage
     */
    public AttackMetadata(DamageMetadata damage, StatMap.CachedStatMap statMap) {
        Validate.notNull(statMap, "StatMap cannot be null");
        Validate.notNull(damage, "Attack cannot be null");

        this.damage = damage;
        this.statMap = statMap;
        this.player = statMap.getPlayer();
    }

    /**
     * @return Is the attack successful, was it cancelled for a reason or another?
     */
    public boolean isSuccessful() {
        return successful;
    }

    public AttackMetadata setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    /**
     * @return Information about the attack
     */
    public DamageMetadata getDamage() {
        return damage;
    }

    public StatMap.CachedStatMap getStats() {
        return statMap;
    }

    public Player getDamager() {
        return player;
    }

    public void damage(LivingEntity target) {
        damage(target, true);
    }

    public void damage(LivingEntity target, boolean knockback) {
        MythicLib.plugin.getDamage().damage(this, target, knockback);
    }
}
