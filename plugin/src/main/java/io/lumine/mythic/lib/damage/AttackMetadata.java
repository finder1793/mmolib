package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.player.PlayerMetadata;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Instanced every time MythicLib detects and monitors one attack from one player.
 *
 * @author Indyuce
 */
public class AttackMetadata extends PlayerMetadata {

    @NotNull
    private final DamageMetadata damage;

    @Nullable
    private final LivingEntity target;

    /**
     * Attacks expire as soon as the corresponding {@link PlayerAttackEvent}
     * was called. This simple boolean makes it easy to keep track of current
     * registered attacks and whether or not this attack can still be used
     * to modify the outcome of the Bukkit damage event.
     */
    private boolean expired;

    /**
     * @deprecated It is now required to provide a target
     */
    @Deprecated
    public AttackMetadata(DamageMetadata damage, PlayerMetadata attacker) {
        this(damage, null, attacker);
    }

    /**
     * Used by AttackHandler instances to register attacks. AttackResult only
     * gives information about the attack damage and types while this class also
     * contains info about the damager. Some plugins don't let MythicLib determine
     * what the damager is so there might be problem with damage/reduction stat
     * application.
     *
     * @param damage   The attack result
     * @param attacker The entity who dealt the damage
     */
    public AttackMetadata(DamageMetadata damage, LivingEntity target, PlayerMetadata attacker) {
        super(attacker);

        Validate.notNull(damage, "Attack cannot be null");

        this.target = target;
        this.damage = damage;
    }

    /**
     * @return Information about the attack
     */
    public DamageMetadata getDamage() {
        return damage;
    }

    public LivingEntity getTarget() {
        return target;
    }

    /**
     * @return Whether or not the corresponding attack is closed.
     */
    public boolean hasExpired() {
        return expired;
    }

    public void expire() {
        Validate.isTrue(!expired, "Attack has expired already");
        expired = true;
    }

    /**
     * @deprecated Cloning is now ambiguous because no target entity is specified. Please use the
     *         new constructor/{@link PlayerMetadata#attack(LivingEntity, double, DamageType...)} instead of cloning
     */
    @Deprecated
    public AttackMetadata clone() {
        return new AttackMetadata(damage.clone(), target, this);
    }

    /**
     * @deprecated There is no longer such a method in the AttackMetadata class.
     *         Use {@link PlayerMetadata#attack(LivingEntity, double, DamageType...)} instead to have a player
     *         deal damage.
     */
    @Deprecated
    public void damage(LivingEntity target) {
        damage(target, true);
    }

    /**
     * @deprecated There is no longer such a method in the AttackMetadata class.
     *         Use {@link PlayerMetadata#attack(LivingEntity, double, DamageType...)} instead to have a player
     *         deal damage.
     */
    @Deprecated
    public void damage(LivingEntity target, boolean knockback) {
        MythicLib.plugin.getDamage().damage(this, target, knockback);
    }
}
