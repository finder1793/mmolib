package io.lumine.mythic.lib.skill.custom.mechanic.type;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.Mechanic;
import io.lumine.mythic.lib.skill.custom.targeter.EntityTargeter;
import io.lumine.mythic.lib.skill.custom.targeter.entity.DefaultEntityTargeter;
import org.bukkit.entity.Entity;

/**
 * A mechanic that takes an entity as parameter. Examples:
 * - damaging mechanics
 * - potion effects
 * - heal, feed, saturate mechanics
 * - tell mechanic
 */
public abstract class TargetMechanic extends Mechanic {
    private final EntityTargeter targeter;

    public TargetMechanic(ConfigObject config) {
        targeter = config.contains("target") ? MythicLib.plugin.getSkills().loadEntityTargeter(config.getObject("target")) : new DefaultEntityTargeter();
    }

    public EntityTargeter getTargeter() {
        return targeter;
    }

    public void cast(SkillMetadata meta) {
        for (Entity target : targeter.findTargets(meta))
            cast(meta, target);
    }

    public abstract void cast(SkillMetadata meta, Entity target);
}