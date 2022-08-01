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
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

@MechanicMetadata
public class RayTraceAnyMechanic extends DirectionMechanic {
    private final double range, size, step;
    private final Script onHitBlock, onHitEntity, onTick;
    private final boolean ignorePassable;

    private static final double DEFAULT_RANGE = 50,
            DEFAULT_SIZE = .2,
            DEFAULT_STEP = .4;

    public RayTraceAnyMechanic(ConfigObject config) {
        super(config);

        onTick = config.contains("tick") ? MythicLib.plugin.getSkills().getScriptOrThrow(config.getString("tick")) : null;
        onHitEntity = config.contains("hit_entity") ? MythicLib.plugin.getSkills().getScriptOrThrow(config.getString("hit_entity")) : null;
        onHitBlock = config.contains("hit_block") ? MythicLib.plugin.getSkills().getScriptOrThrow(config.getString("hit_block")) : null;
        ignorePassable = config.getBoolean("ignore-passable", false);

        range = config.getDouble("range", DEFAULT_RANGE);
        size = config.getDouble("size", DEFAULT_SIZE);
        step = config.getDouble("step", DEFAULT_STEP);

        Validate.isTrue(range > 0, "Range must be strictly positive");
        Validate.isTrue(size >= 0, "Size must be positive or null");
        Validate.isTrue(step > 0, "Step must be strictly positive (don't make it too low)");
    }

    @Override
    public void cast(SkillMetadata meta, Location source, Vector dir) {
        Validate.isTrue(dir.lengthSquared() > 0, "Direction cannot be zero");

        dir.normalize();

        /*
         * There seems to be an issue where this does NOT
         * stop on blocks.... TODO
         */
        RayTraceResult result = source.getWorld().rayTrace(source, dir, range, FluidCollisionMode.NEVER, ignorePassable, size, entity -> entity instanceof LivingEntity && !entity.equals(meta.getCaster().getPlayer()));
        double length = result == null ? range : result.getHitPosition().distance(source.toVector());

        if (onTick != null)
            for (double j = 0; j < length; j += step) {
                Location intermediate = source.clone().add(dir.clone().multiply(j));
                onTick.cast(meta.clone(source, intermediate, null, null));
            }

        if (result == null)
            return;

        Location hitPosition = result.getHitPosition().toLocation(source.getWorld());

        if (onHitBlock != null && result.getHitBlock() != null)
            onHitBlock.cast(meta.clone(source, hitPosition, null, null));

        if (onHitEntity != null && result.getHitEntity() != null)
            onHitEntity.cast(meta.clone(source, hitPosition, result.getHitEntity(), null));
    }
}