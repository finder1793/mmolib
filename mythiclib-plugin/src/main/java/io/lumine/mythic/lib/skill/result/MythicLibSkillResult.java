package io.lumine.mythic.lib.skill.result;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.Script;
import io.lumine.mythic.lib.script.condition.Condition;

public class MythicLibSkillResult implements SkillResult {
    private final Script skill;

    public MythicLibSkillResult(Script skill) {
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
