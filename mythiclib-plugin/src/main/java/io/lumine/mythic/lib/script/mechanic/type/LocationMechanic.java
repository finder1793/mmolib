package io.lumine.mythic.lib.script.mechanic.type;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.script.targeter.LocationTargeter;
import io.lumine.mythic.lib.script.targeter.location.DefaultLocationTargeter;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.mechanic.Mechanic;
import org.bukkit.Location;

/**
 * A mechanic that takes a location as parameter. Examples:
 * - particle and sound mechanics
 * - lightning mechanic
 */
public abstract class LocationMechanic extends Mechanic {
    private final LocationTargeter targeter;

    public LocationMechanic(ConfigObject config) {
        this.targeter = config.contains("target") ? MythicLib.plugin.getSkills().loadLocationTargeter(config.getObject("target")) : new DefaultLocationTargeter();
    }

    public LocationTargeter getTargeter() {
        return targeter;
    }

    @Override
    public void cast(SkillMetadata meta) {
        for (Location loc : targeter.findTargets(meta))
            cast(meta, loc);
    }

    public abstract void cast(SkillMetadata meta, Location loc);
}
