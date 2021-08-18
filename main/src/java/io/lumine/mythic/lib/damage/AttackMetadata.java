package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.api.stat.StatMap;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

/**
 * Instanced every time MythicLib detects and monitors one attack from one player.
 *
 * @author Indyuce
 */
public class AttackMetadata {
    private final Player player;
    private final StatMap.CachedStatMap stats;
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
     * @param damager The entity who dealt the damage
     */
    public AttackMetadata(DamageMetadata damage, StatMap.CachedStatMap damager) {
        Validate.notNull(damager, "Damager cannot be null");
        Validate.notNull(damage, "Attack cannot be null");

        this.damage = damage;
        this.stats = damager;
        this.player = stats.getPlayer();
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
        return stats;
    }

    public Player getDamager() {
        return stats.getPlayer();
    }
}
