package io.lumine.mythic.lib.skill.handler;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.Script;
import io.lumine.mythic.lib.script.MechanicQueue;
import io.lumine.mythic.lib.skill.result.MythicLibSkillResult;

/**
 * A skill behaviour based on a custom MythicLib script
 */
public class MythicLibSkillHandler extends SkillHandler<MythicLibSkillResult> {
    private final Script skill;

    public MythicLibSkillHandler(Script skill) {
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
