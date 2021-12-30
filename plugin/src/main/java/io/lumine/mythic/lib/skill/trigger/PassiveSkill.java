package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.modifier.ModifierSource;
import io.lumine.mythic.lib.player.PlayerModifier;
import io.lumine.mythic.lib.skill.Skill;

/**
 * There is one skill trigger instance per passive skill the player
 * has. A passive skill can be registered by MMOItems items or MMOCore
 * passive skills.
 *
 * @author indyuce
 */
public class PassiveSkill extends PlayerModifier {

    /**
     * Skill cast whenever the action is performed
     */
    private final Skill triggered;

    private final TriggerType type;

    /**
     * Identifier given to skills to differentiate between them.
     * Every plugin like MMOItems has a key to be able to manipulate
     * the triggers that were registered on the player at any time
     */
    private final String key;

    /**
     * @param key            A key like 'item' or 'itemSet' indicating what is giving a passive skill to the player.
     *                       There can be multiple skills with the same key, it's not a unique identifier.
     *                       It can be later used to isolate and unregister skills with a certain key.
     * @param type           When that skill should trigger
     * @param triggered      The skill
     * @param equipmentSlot  The equipment slot granting this passive skill
     * @param modifierSource The source of the passive skill
     */
    public PassiveSkill(String key, TriggerType type, Skill triggered, EquipmentSlot equipmentSlot, ModifierSource modifierSource) {
        super(equipmentSlot, modifierSource);

        this.key = key;
        this.type = type;
        this.triggered = triggered;
    }

    /**
     * @param key       A key like 'item' or 'itemSet' indicating what is giving a passive skill to the player.
     *                  There can be multiple skills with the same key, it's not a unique identifier.
     *                  It can be later used to isolate and unregister skills with a certain key.
     * @param type      When that skill should trigger
     * @param triggered The skill
     * @deprecated Equipment slot and modifier source required in MythicLib 1.2+
     */
    @Deprecated
    public PassiveSkill(String key, TriggerType type, Skill triggered) {
        super(EquipmentSlot.OTHER, ModifierSource.OTHER);

        this.key = key;
        this.type = type;
        this.triggered = triggered;
    }

    public Skill getTriggeredSkill() {
        return triggered;
    }

    public TriggerType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
