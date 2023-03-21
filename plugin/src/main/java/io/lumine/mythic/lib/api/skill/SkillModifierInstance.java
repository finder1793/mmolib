package io.lumine.mythic.lib.api.skill;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.api.ModifiedInstance;
import io.lumine.mythic.lib.skill.Skill;

import java.util.function.Function;
import java.util.function.Predicate;

public class SkillModifierInstance extends ModifiedInstance<SkillBuff> {
    private final String skillModifier;

    public SkillModifierInstance(String skillModifier) {
        this.skillModifier = skillModifier;
    }

    /**
     * @return The final skillModifier value taking into account
     * the base value of the modifier & the skillBuffs applied to it.
     * The relative skill buffs are applied afterwards, onto the sum of the base value + flat
     * modifiers.
     */
    public double getTotal(Skill skill) {
        return getFilteredTotal(skill, EquipmentSlot.MAIN_HAND::isCompatible);
    }

    /**
     * @param filter Filters skill modifications taken into account for the calculation
     * @return The final skill modifier value taking into account the default modifier value
     * as well as the skill buffs. The relative skill buffs are
     * applied afterwards, onto the sum of the base value + flat
     * buffs.
     */
    public double getFilteredTotal(Skill skill, Predicate<SkillBuff> filter) {
        return getFilteredTotal(skill, filter, mod -> mod);
    }

    /**
     * @param modification A modification to any skill buff before taking it into
     *                     account in skill modifier calculation. This can be used for instance to
     *                     reduce debuffs, by checking if a skill buff has a negative
     *                     value and returning a buff with a reduced absolute value
     * @return The final skillModifier value taking into account
     * the base value of the modifier & the skillBuffs applied to it.
     * The relative skill buffs are applied afterwards, onto the sum of the base value + flat
     * modifiers.
     */
    public double getTotal(Skill skill, Function<SkillBuff, SkillBuff> modification) {
        return getFilteredTotal(skill, EquipmentSlot.MAIN_HAND::isCompatible, modification);
    }

    /**
     * @param filter       Filters skill buffs taken into account for the calculation
     * @param modification A modification to any skill buff before taking it into
     *                     account in skill modifier calculation. This can be used for instance to
     *                     reduce debuffs, by checking if a skill buff has a negative
     *                     value and returning a buff with a reduced absolute value
     * @return The final skillModifier value taking into account
     * * the base value of the modifier & the skillBuffs applied to it.
     * * The relative skill buffs are applied afterwards, onto the sum of the base value + flat
     * * modifiers.
     */
    public double getFilteredTotal(Skill skill, Predicate<SkillBuff> filter, Function<SkillBuff, SkillBuff> modification) {
        return getFilteredTotal(skill.getModifier(skillModifier), filter, modification);
    }


}
