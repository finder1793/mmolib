package io.lumine.mythic.lib.skill.condition.def;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.condition.Condition;
import io.lumine.mythic.lib.util.ConfigObject;
import org.bukkit.entity.LivingEntity;

/**
 * Checks if the target entity is a living entity. This should
 * also be used to check if an entity has a health bar
 */
public class IsLivingCondition extends Condition {
    private final boolean caster;

    public IsLivingCondition(ConfigObject config) {
        super(config);

        caster = config.getBoolean("caster", false);
    }

    @Override
    public boolean isMet(SkillMetadata meta) {
        return meta.getSkillEntity(caster) instanceof LivingEntity;
    }
}