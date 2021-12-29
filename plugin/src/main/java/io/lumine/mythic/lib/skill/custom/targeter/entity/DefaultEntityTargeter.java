package io.lumine.mythic.lib.skill.custom.targeter.entity;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.targeter.EntityTargeter;
import io.lumine.mythic.lib.skill.custom.targeter.location.DefaultLocationTargeter;
import org.bukkit.entity.Entity;

import java.util.Arrays;
import java.util.List;

/**
 * Uses the target entity if not null or caster by default.
 * <p>
 * Analog of {@link DefaultLocationTargeter}
 */
public class DefaultEntityTargeter implements EntityTargeter {

    @Override
    public List<Entity> findTargets(SkillMetadata meta) {
        return Arrays.asList(meta.getSkillEntity(false));
    }
}
