package io.lumine.mythic.lib.skill.result;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.CustomSkill;
import io.lumine.mythic.lib.skill.custom.condition.Condition;

public class CustomSkillResult implements SkillResult {
    private final CustomSkill skill;

    public CustomSkillResult(CustomSkill skill) {
        this.skill = skill;
    }

    @Override
    public boolean isSuccessful(SkillMetadata skillMeta) {
        for (Condition condition : skill.getConditions())
            if (!condition.checkIfMet(skillMeta))
                return false;
        return true;
    }
}
