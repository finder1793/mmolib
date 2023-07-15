package io.lumine.mythic.lib.skill.result.def;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.result.SkillResult;

public class SimpleSkillResult implements SkillResult {
    private final boolean successful;

    public SimpleSkillResult() {
        this(true);
    }

    public SimpleSkillResult(boolean successful) {
        this.successful = successful;
    }

    @Override
    public boolean isSuccessful(SkillMetadata skillMeta) {
        return successful;
    }
}
