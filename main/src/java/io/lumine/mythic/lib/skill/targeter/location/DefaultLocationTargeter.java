package io.lumine.mythic.lib.skill.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.targeter.LocationTargeter;
import io.lumine.mythic.lib.util.EntityLocationType;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Takes target location or source location if null/prioritized
 */
public class DefaultLocationTargeter implements LocationTargeter {

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(meta.getSkillLocation(false));
    }
}
