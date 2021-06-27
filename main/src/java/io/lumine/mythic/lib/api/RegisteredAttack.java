package io.lumine.mythic.lib.api;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;

/**
 * Instanced every time MythicLib detects and monitors one attack from one player.
 *
 * @author Indyuce
 */
public class RegisteredAttack {
    private final AttackResult result;
    private final LivingEntity damager;

    /**
     * Used by DamageHandler instances to register attacks. AttackResult only
     * gives information about the attack damage and types while this class also
     * contains info about the damager. Some plugins don't let MMOLib determine
     * what the damager is so there might be problem with damage/reduction stat
     * application.
     *
     * @param result  The attack result
     * @param damager The entity who dealt the damage
     */
    public RegisteredAttack(AttackResult result, LivingEntity damager) {
        // Validate.notNull(damager, "Damager cannot be null");
        Validate.notNull(result, "Attack cannot be null");

        this.result = result;
        this.damager = damager;
    }

    /**
     * @return Information about the attack
     */
    public AttackResult getResult() {
        return result;
    }

    /**
     * @return The entity who dealt the damage
     */
    public @Nullable
    LivingEntity getDamager() {
        return damager;
    }
}
