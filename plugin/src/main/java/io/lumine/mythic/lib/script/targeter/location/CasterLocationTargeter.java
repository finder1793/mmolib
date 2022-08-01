package io.lumine.mythic.lib.script.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.targeter.LocationTargeter;
import io.lumine.mythic.lib.util.EntityLocationType;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Takes the location of the target entity. Just like {@link TargetEntityLocationTargeter}
 * you can provide the position of the entity body you want to use as location
 */
public class CasterLocationTargeter extends LocationTargeter {
    private final EntityLocationType entityLocationType;

    public CasterLocationTargeter(ConfigObject config) {
        super(false);

        this.entityLocationType = config.contains("position") ? EntityLocationType.valueOf(config.getString("position").toUpperCase()) : EntityLocationType.BODY;
    }

    public CasterLocationTargeter(EntityLocationType locType) {
        super(false);

        this.entityLocationType = locType;
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(entityLocationType.getLocation(meta.getCaster().getPlayer()));
    }
}
