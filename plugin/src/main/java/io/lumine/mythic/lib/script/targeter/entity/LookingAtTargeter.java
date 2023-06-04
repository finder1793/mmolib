package io.lumine.mythic.lib.script.targeter.entity;

import io.lumine.mythic.lib.script.targeter.EntityTargeter;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.apache.commons.lang.Validate;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Targets the entity the player is looking at
 */
public class LookingAtTargeter implements EntityTargeter {
    private final double range, size;
    private final boolean ignorePassable;

    public LookingAtTargeter(ConfigObject config) {
        size = config.getDouble("size", .2f);
        Validate.isTrue(size >= 0, "Size must be positive");
        range = config.getDouble("length", 50);
        Validate.isTrue(range > 0, "Length must be strictly positive");
        ignorePassable = config.getBoolean("ignore_passable", true);
    }

    @Override
    public List<Entity> findTargets(SkillMetadata meta) {
        Location source = meta.getCaster().getPlayer().getEyeLocation();
        RayTraceResult result = meta.getSourceLocation().getWorld().rayTrace(source, source.getDirection(), range, FluidCollisionMode.NEVER, ignorePassable, size, entity -> true);
        return result == null || result.getHitEntity() == null ? new ArrayList<>() : Arrays.asList(result.getHitEntity());
    }
}
