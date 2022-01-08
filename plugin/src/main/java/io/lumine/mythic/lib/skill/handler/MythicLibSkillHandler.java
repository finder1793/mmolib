package io.lumine.mythic.lib.skill.handler;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.CustomSkill;
import io.lumine.mythic.lib.skill.custom.MechanicQueue;
import io.lumine.mythic.lib.skill.result.MythicLibSkillResult;

/**
 * A skill behaviour based on a custom MythicLib skill
 */
public class MythicLibSkillHandler extends SkillHandler<MythicLibSkillResult> {
    private final CustomSkill skill;

    public MythicLibSkillHandler(CustomSkill skill) {
        super(skill.getId());

        this.skill = skill;
    }

    @Override
    public MythicLibSkillResult getResult(SkillMetadata meta) {
        return new MythicLibSkillResult(skill);
    }

    @Override
    public void whenCast(MythicLibSkillResult result, SkillMetadata skillMeta) {
        new MechanicQueue(skillMeta, skill).next();
    }
}
