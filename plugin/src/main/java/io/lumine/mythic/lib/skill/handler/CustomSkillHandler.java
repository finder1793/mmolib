package io.lumine.mythic.lib.skill.handler;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.CustomSkill;
import io.lumine.mythic.lib.skill.custom.MechanicQueue;
import io.lumine.mythic.lib.skill.result.CustomSkillResult;
import io.lumine.mythic.lib.skill.result.SkillResult;

/**
 * A skill behaviour based on a custom MythicLib skill
 */
public class CustomSkillHandler extends SkillHandler<CustomSkillResult> {
    private final CustomSkill skill;

    public CustomSkillHandler(CustomSkill skill) {
        super(skill.getId());

        this.skill = skill;
    }

    @Override
    public CustomSkillResult getResult(SkillMetadata meta) {
        return new CustomSkillResult(skill);
    }

    @Override
    public void whenCast(CustomSkillResult result, SkillMetadata skillMeta) {
        new MechanicQueue(skillMeta, skill).next();
    }
}
