package io.lumine.mythic.lib.skill.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.targeter.LocationTargeter;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.EntityLocationType;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Takes the location of the target entity. Just like {@link CasterLocationTargeter}
 * you can provide the position of the entity body you want to use as location
 */
public class TargetEntityLocationTargeter implements LocationTargeter {
    private final EntityLocationType entityLocationType;

    public TargetEntityLocationTargeter(ConfigObject config) {
        this.entityLocationType = config.contains("position") ? EntityLocationType.valueOf(config.getString("position").toUpperCase()) : EntityLocationType.BODY;
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(entityLocationType.getLocation(meta.getTargetEntity()));
    }
}
