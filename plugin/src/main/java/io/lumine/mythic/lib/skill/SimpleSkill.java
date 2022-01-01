package io.lumine.mythic.lib.skill;

import io.lumine.mythic.lib.skill.handler.SkillHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Can be used to cast a skill handler without modifiers
 *
 * @author jules
 */
public class SimpleSkill extends Skill {
    private final SkillHandler<?> handler;

    public SimpleSkill(SkillHandler<?> handler) {
        this.handler = handler;
    }

    @NotNull
    @Override
    public boolean getResult(SkillMetadata skillMeta) {
        return true;
    }

    @Override
    public void whenCast(SkillMetadata skillMeta) {
        // Nothing here
    }

    @Override
    public SkillHandler<?> getHandler() {
        return handler;
    }

    @Override
    public double getModifier(String path) {
        return 0;
    }
}
