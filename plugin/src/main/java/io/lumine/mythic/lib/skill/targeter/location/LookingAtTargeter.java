package io.lumine.mythic.lib.skill.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.targeter.LocationTargeter;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Targets the block the player is looking at. Precisely, it uses a ray trace
 * to find the exact location where his line of sight hits the target block.
 */
public class LookingAtTargeter implements LocationTargeter {
    private final double rayCastLength;
    private final boolean ignorePassable;

    public LookingAtTargeter(ConfigObject config) {
        this.rayCastLength = config.getDouble("length", 50);
        ignorePassable = config.getBoolean("ignore-passable", false);
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        Location source = meta.getCaster().getPlayer().getEyeLocation();
        RayTraceResult result = meta.getSourceLocation().getWorld().rayTraceBlocks(source, source.getDirection(), rayCastLength, FluidCollisionMode.NEVER, ignorePassable);
        return result == null ? new ArrayList<>() : Arrays.asList(result.getHitPosition().toLocation(source.getWorld()));
    }
}
