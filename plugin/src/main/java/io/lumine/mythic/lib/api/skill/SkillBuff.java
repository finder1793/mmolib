package io.lumine.mythic.lib.api.skill;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.api.InstanceModifier;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.ModifierType;

import java.util.ArrayList;
import java.util.List;

public class SkillBuff extends InstanceModifier {
    /**
     * The list of all the skills this buff will be applied to.
     * A skill buff can target one skill or a set of skills like giving
     * for example +10% damage to all the passive skills.
     */
    private final List<String> skills;
    private final String modifier;

    /**
     * Flat skill buff (simplest modifier you can think about)
     */
    public SkillBuff(String key, String modifier, List<String> skills, double value) {
        this(key, modifier, skills, value, ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    /**
     * skill buff given by an external mechanic, like a party buff, item set bonuses,
     * skills or abilities... Anything apart from items and armor.
     */
    public SkillBuff(String key, String modifier, List<String> skills, double value, ModifierType type) {
        this(key, modifier, skills, value, type, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    public SkillBuff(String key, String modifier, List<String> skills, double value, ModifierType type, EquipmentSlot slot, ModifierSource source) {
        super(key, slot, source, value, type);
        this.skills = skills;
        this.modifier = modifier;
    }

    /**
     * Used to add a constant to some existing stat modifier, usually an
     * integer, for instance it is used when a skill buff trigger is triggered multiple times.
     *
     * @param offset The offset added.
     * @return A new instance of SkillBuff with modified value
     */
    public SkillBuff add(double offset) {
        return new SkillBuff(getKey(), modifier, new ArrayList(skills), value + offset, type, getSlot(), getSource());
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getModifier() {
        return modifier;
    }


    /**
     * Used to register the skillBuff for only 1 specific skill.
     */
    public void register(MMOPlayerData playerData, String skill) {
        playerData.getSkillBuffMap().addSkillBuff(this);
        playerData.getSkillBuffMap().getSkillInstance(skill).getSkillModifier(modifier).addModifier(this);
    }
    /**
     * Used to unregister the skillBuff for only 1 specific skill.
     */
    public void unregister(MMOPlayerData playerData, String skill) {
        playerData.getSkillBuffMap().removeSkillBuff(getKey());
        playerData.getSkillBuffMap().getSkillInstance(skill).getSkillModifier(modifier).remove(getKey());
    }

    @Override
    public void register(MMOPlayerData playerData) {
        playerData.getSkillBuffMap().addSkillBuff(this);
        for (String skill : skills)
            playerData.getSkillBuffMap().getSkillInstance(skill).getSkillModifier(modifier).addModifier(this);
    }


    @Override
    public void unregister(MMOPlayerData playerData) {
        playerData.getSkillBuffMap().removeSkillBuff(getKey());
        for (String skill : skills)
            playerData.getSkillBuffMap().getSkillInstance(skill).getSkillModifier(modifier).remove(getKey());
    }
}
