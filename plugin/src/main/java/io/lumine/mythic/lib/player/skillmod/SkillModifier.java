package io.lumine.mythic.lib.player.skillmod;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.api.InstanceModifier;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.ModifierType;

import java.util.ArrayList;
import java.util.List;

/**
 * A skill "modifier" modifies a specific parameter of a skill,
 * in the same way that a stat modifier modifies a stat for a player.
 * It can also be given a boolean formula, which determines which
 * skills the modifier will apply onto.
 *
 * @author Guillaume, Jules
 */
public class SkillModifier extends InstanceModifier {

    /**
     * The list of all the skills this modifier will be applied to.
     * A skill modifier can target one skill or a set of skills like
     * giving for example +10% damage to all the passive skills.
     */
    private final List<String> skills;
    private final String parameter;

    /**
     * Flat skill buff (simplest modifier you can think about)
     */
    public SkillModifier(String key, String parameter, List<String> skills, double value) {
        this(key, parameter, skills, value, ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    /**
     * skill buff given by an external mechanic, like a party buff, item set bonuses,
     * skills or abilities... Anything apart from items and armor.
     */
    public SkillModifier(String key, String parameter, List<String> skills, double value, ModifierType type) {
        this(key, parameter, skills, value, type, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    public SkillModifier(String key, String parameter, List<String> skills, double value, ModifierType type, EquipmentSlot slot, ModifierSource source) {
        super(key, slot, source, value, type);

        this.skills = skills;
        this.parameter = parameter;
    }

    /**
     * Used to add a constant to some existing stat modifier, usually an
     * integer, for instance it is used when a skill buff trigger is triggered multiple times.
     *
     * @param offset The offset added.
     * @return A new instance of SkillBuff with modified value
     */
    public SkillModifier add(double offset) {
        return new SkillModifier(getKey(), parameter, new ArrayList(skills), value + offset, type, getSlot(), getSource());
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getParameter() {
        return parameter;
    }

    /**
     * Used to register the skillBuff for only 1 specific skill.
     */
    public void register(MMOPlayerData playerData, String skill) {
        playerData.getSkillBuffMap().addSkillBuff(this);
        SkillInstance skillInstance = playerData.getSkillBuffMap().getSkillInstance(skill);
        if (skillInstance.hasModifier(parameter))
            skillInstance.getSkillModifier(parameter).addModifier(this);
    }

    /**
     * Used to unregister the skillBuff for only 1 specific skill.
     */
    public void unregister(MMOPlayerData playerData, String skill) {
        playerData.getSkillBuffMap().removeSkillBuff(getKey());
        SkillInstance skillInstance = playerData.getSkillBuffMap().getSkillInstance(skill);
        if (skillInstance.hasModifier(parameter))
            skillInstance.getSkillModifier(parameter).remove(getKey());
    }

    @Override
    public void register(MMOPlayerData playerData) {
        playerData.getSkillBuffMap().addSkillBuff(this);
        for (String skill : skills) {
            SkillInstance skillInstance = playerData.getSkillBuffMap().getSkillInstance(skill);
            if (skillInstance.hasModifier(parameter))
                skillInstance.getSkillModifier(parameter).addModifier(this);
        }
    }

    @Override
    public void unregister(MMOPlayerData playerData) {
        playerData.getSkillBuffMap().removeSkillBuff(getKey());
        for (String skill : skills) {
            SkillInstance skillInstance = playerData.getSkillBuffMap().getSkillInstance(skill);
            if (skillInstance.hasModifier(parameter))
                skillInstance.getSkillModifier(parameter).remove(getKey());
        }
    }
}
