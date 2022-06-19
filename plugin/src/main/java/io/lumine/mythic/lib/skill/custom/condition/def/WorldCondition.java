package io.lumine.mythic.lib.skill.custom.condition.def;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.condition.Condition;
import io.lumine.mythic.lib.util.configobject.ConfigObject;

/**
 * Checks if the caster's world is XX
 */
public class WorldCondition extends Condition {
    private final String worldName;

    public WorldCondition(ConfigObject config) {
        super(config);

        config.validateKeys("name");
        worldName = config.getString("name");
    }

    @Override
    public boolean isMet(SkillMetadata meta) {
        return meta.getSourceLocation().getWorld().getName().equalsIgnoreCase(worldName);
    }
}