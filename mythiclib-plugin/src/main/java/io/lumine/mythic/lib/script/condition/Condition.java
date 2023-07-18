package io.lumine.mythic.lib.script.condition;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;

public abstract class Condition {
    private final boolean reversed;

    public Condition(ConfigObject config) {
        this.reversed = config.getBoolean("reverse", false);
    }

    /**
     * This is the method which needs to be called outside
     * of the {@link Condition} class as it DOES take into
     * account reverse (or normal) conditions
     *
     * @param meta Meta of skill being cast
     * @return If that condition is met
     */
    public boolean checkIfMet(SkillMetadata meta) {
        return reversed ? !isMet(meta) : isMet(meta);
    }

    public abstract boolean isMet(SkillMetadata meta);
}
