package io.lumine.mythic.lib.api.skill;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.api.InstanceModifier;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import io.lumine.mythic.lib.player.modifier.PlayerModifier;

public class SkillBuff extends InstanceModifier {
    private final String skill, modifier;

    /**
     * Flat stat modifier (simplest modifier you can think about)
     */
    public SkillBuff(String key, String modifier, String stat, double value) {
        this(key, stat, modifier, value, ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    /**
     * Stat modifier given by an external mecanic, like a party buff, item set bonuses,
     * skills or abilities... Anything apart from items and armor.
     */
    public SkillBuff(String key, String modifier, String stat, double value, ModifierType type) {
        this(key, stat, modifier, value, type, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    public SkillBuff(String key, String modifier, String skill, double value, ModifierType type, EquipmentSlot slot, ModifierSource source) {
        super(key, slot, source,value,type);
        this.skill = skill;
        this.modifier = modifier;
    }

    public String getSkill() {
        return skill;
    }

    public String getModifier() {
        return modifier;
    }

    @Override
    public void register(MMOPlayerData playerData) {
        playerData.getSkillBuffMap().getSkillInstance(skill).getSkillModifier(modifier).addModifier(this);
    }


    @Override
    public void unregister(MMOPlayerData playerData) {
        playerData.getSkillBuffMap().getSkillInstance(skill).getSkillModifier(modifier).remove(getKey());
    }
}
