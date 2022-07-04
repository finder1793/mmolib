package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.player.PlayerMetadata;

/**
 * Used by melee attacks with melee weapons like custom
 * or vanilla swords, axes...
 */
public class MeleeAttackMetadata extends AttackMetadata {

    /**
     * This is used to support the following plugins which implement
     * off hand item attacks.
     * - RealDualWield
     * - DualWield
     */
    private final EquipmentSlot hand;

    /**
     * Used by AttackHandler instances to register attacks. AttackResult only
     * gives information about the attack damage and types while this class also
     * contains info about the damager. Some plugins don't let MythicLib determine
     * what the damager is so there might be problem with damage/reduction stat
     * application.
     *
     * @param damage   The attack result
     * @param attacker The entity who dealt the damage
     * @param hand Hand used in the melee attack
     */
    public MeleeAttackMetadata(DamageMetadata damage, PlayerMetadata attacker, EquipmentSlot hand) {
        super(damage, attacker);

        this.hand = hand;
    }

    public EquipmentSlot getHand() {
        return hand;
    }
}
