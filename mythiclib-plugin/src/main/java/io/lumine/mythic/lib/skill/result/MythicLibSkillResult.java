package io.lumine.mythic.lib.skill.result;

import io.lumine.mythic.lib.script.Script;
import io.lumine.mythic.lib.script.condition.Condition;
import io.lumine.mythic.lib.skill.SkillMetadata;
import org.jetbrains.annotations.NotNull;

public class MythicLibSkillResult implements SkillResult {
    private final boolean success;

    public MythicLibSkillResult(@NotNull SkillMetadata skillMeta, @NotNull Script script) {
        this.success = checkConditions(skillMeta, script);
    }

    @Override
    public boolean isSuccessful() {
        return success;
    }

    private boolean checkConditions(@NotNull SkillMetadata skillMeta, @NotNull Script script) {
        for (Condition condition : script.getConditions())
            if (!condition.checkIfMet(skillMeta)) return false;
        return true;
    }
}
