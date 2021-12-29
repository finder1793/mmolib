package io.lumine.mythic.lib.skill;

import io.lumine.mythic.lib.comp.mythicmobs.MythicSkillInfo;
import io.lumine.mythic.lib.skill.handler.SkillHandler;

/**
 * Implemented by MMOItems abilities or MMOCore class skills
 */
public interface Skill extends MythicSkillInfo {
    public SkillHandler getHandler();
}
