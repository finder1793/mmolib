package io.lumine.mythic.lib.api.event.skill;

import io.lumine.mythic.lib.api.event.MMOPlayerDataEvent;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.result.SkillResult;

public abstract class PlayerSkillEvent extends MMOPlayerDataEvent {
    private final SkillMetadata skillMeta;
    private final SkillResult result;

    public PlayerSkillEvent(SkillMetadata skillMeta, SkillResult result) {
        super(skillMeta.getCaster().getData());

        this.skillMeta = skillMeta;
        this.result = result;
    }

    public Skill getCast() {
        return skillMeta.getCast();
    }

    public SkillResult getResult() {
        return result;
    }

    public SkillMetadata getMetadata() {
        return skillMeta;
    }
}
