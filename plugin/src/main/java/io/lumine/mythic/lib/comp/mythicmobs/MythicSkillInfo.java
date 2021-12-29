package io.lumine.mythic.lib.comp.mythicmobs;

import io.lumine.mythic.lib.skill.Skill;

/**
 * @deprecated Being replaced by {@link Skill}
 */
@Deprecated
public interface MythicSkillInfo {
    public double getModifier(String path);
}
