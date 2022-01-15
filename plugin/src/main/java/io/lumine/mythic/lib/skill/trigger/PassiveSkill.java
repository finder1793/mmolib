package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.PlayerModifier;
import io.lumine.mythic.lib.skill.Skill;

/**
 * There is one TriggeredSkill instance per active/passive skill the player
 * has. A triggered skill can be registered by MMOItems items or MMOCore
 * passive skills.
 * <p>
 * The distinction between active and passive skills is pretty vague.
 * <p>
 * In MMOItems:
 * we consider a skill that is cast when right clicking active (for
 * instance when holding an item) even though it's handled just like
 * an on-hit passive skill within MythicLib. Pretty confusing
 * <p>
 * In MMOCore:
 * Skills that are cast in MMOCore using the casting mode are also
 * active and any skill that has to be triggered is passive. It's
 * much less confusing here
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
     * @param key            A key like 'item' or 'itemSet' indicating what is giving a triggered skill to the player.
     *                       There can be multiple skills with the same key, it's not a unique identifier.
     *                       It can be later used to isolate and unregister skills with a certain key.
     * @param type           When that skill should trigger
     * @param triggered      The skill
     * @param equipmentSlot  The equipment slot granting this passive skill
     * @param modifierSource The source of the passive skill
     */
    public PassiveSkill(String key, TriggerType type, Skill triggered, EquipmentSlot equipmentSlot, ModifierSource modifierSource) {
        super(key, equipmentSlot, modifierSource);

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
        super(key, EquipmentSlot.OTHER, ModifierSource.OTHER);

        this.type = type;
        this.triggered = triggered;
    }

    public Skill getTriggeredSkill() {
        return triggered;
    }

    public TriggerType getType() {
        return type;
    }
}
