package io.lumine.mythic.lib.script.condition.misc;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.condition.Condition;

/**
 * Checks if the target entity is on fire
 */
public class OnFireCondition extends Condition {
    private final boolean caster;

    public OnFireCondition(ConfigObject config) {
        super(config);

        caster = config.getBoolean("caster", false);
    }

    @Override
    public boolean isMet(SkillMetadata meta) {
        return meta.getSkillEntity(caster).getFireTicks() > 0;
    }
}