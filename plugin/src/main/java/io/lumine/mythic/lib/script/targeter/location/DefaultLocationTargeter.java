package io.lumine.mythic.lib.script.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.targeter.LocationTargeter;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Takes target location or source location if null/prioritized
 */
public class DefaultLocationTargeter extends LocationTargeter {
    public DefaultLocationTargeter() {
        super(false);
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(meta.getSkillLocation(false));
    }
}
