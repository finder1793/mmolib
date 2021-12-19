package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.modifier.ModifierSource;
import io.lumine.mythic.lib.player.PlayerModifier;

/**
 * There is one skill trigger instance per passive skill the player
 * has. A passive skill can be registered by MMOItems items or MMOCore
 * passive skills.
 *
 * @author indyuce
 */
public class PassiveSkill extends PlayerModifier {

    /**
     * Triggered whenever the action is performed
     */
    private final TriggeredSkill triggered;

    private final TriggerType type;

    /**
     * Identifier given to skills to differenciate every of them.
     * Every plugin like MMOItems has a key to be able to manipulate
     * the triggers that were registered on the player at any time
     */
    private final String key;

    /**
     * @param key            A key like 'item' or 'itemSet' indicating what is giving a passive skill to the player
     * @param type           When that skill should trigger
     * @param triggered      The skill
     * @param equipmentSlot  The equipment slot granting this passive skill
     * @param modifierSource The source of the passive skill
     */
    public PassiveSkill(String key, TriggerType type, TriggeredSkill triggered, EquipmentSlot equipmentSlot, ModifierSource modifierSource) {
        super(equipmentSlot, modifierSource);

        this.key = key;
        this.type = type;
        this.triggered = triggered;
    }

    /**
     * @param key       A key like 'item' or 'itemSet' indicating what is giving a passive skill to the player
     * @param type      When that skill should trigger
     * @param triggered The skill
     * @deprecated Equipment slot and modifier source required in MythicLib 1.2+
     */
    @Deprecated
    public PassiveSkill(String key, TriggerType type, TriggeredSkill triggered) {
        super(EquipmentSlot.OTHER, ModifierSource.OTHER);

        this.key = key;
        this.type = type;
        this.triggered = triggered;
    }

    public TriggeredSkill getTriggeredSkill() {
        return triggered;
    }

    public TriggerType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
