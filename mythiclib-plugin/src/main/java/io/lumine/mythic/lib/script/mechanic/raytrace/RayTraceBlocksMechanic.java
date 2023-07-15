package io.lumine.mythic.lib.script.mechanic.raytrace;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.Script;
import io.lumine.mythic.lib.script.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.script.mechanic.type.DirectionMechanic;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.apache.commons.lang.Validate;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

@MechanicMetadata
public class RayTraceBlocksMechanic extends DirectionMechanic {
    private final double range, step;
    private final Script onHit, onTick;
    private final boolean ignorePassable;

    private static final double DEFAULT_RANGE = 50,
            DEFAULT_STEP = .4;

    public RayTraceBlocksMechanic(ConfigObject config) {
        super(config);

        onTick = config.contains("tick") ? MythicLib.plugin.getSkills().getScriptOrThrow(config.getString("tick")) : null;
        onHit = config.contains("hit_block") ? MythicLib.plugin.getSkills().getScriptOrThrow(config.getString("hit_block")) : null;
        ignorePassable = config.getBoolean("ignore_passable", false);

        range = config.getDouble("range", DEFAULT_RANGE);
        step = config.getDouble("step", DEFAULT_STEP);

        Validate.isTrue(range > 0, "Range must be strictly positive");
        Validate.isTrue(step > 0, "Step must be strictly positive (don't make it too low)");
    }

    @Override
    public void cast(SkillMetadata meta, Location source, Vector dir) {
        Validate.isTrue(dir.lengthSquared() > 0, "Direction cannot be zero");

        dir.normalize();
        RayTraceResult result = source.getWorld().rayTraceBlocks(source, dir, range, FluidCollisionMode.NEVER, ignorePassable);
        double length = result == null ? range : result.getHitPosition().distance(source.toVector());

        if (onTick != null)
            for (double j = 0; j < length; j += step) {
                Location intermediate = source.clone().add(dir.clone().multiply(j));
                onTick.cast(meta.clone(source, intermediate, null, null));
            }

        if (result != null && onHit != null && result.getHitBlock() != null) {
            Location hitPosition = result.getHitPosition().toLocation(source.getWorld());
            onHit.cast(meta.clone(source, hitPosition, null, null));
        }
    }
}