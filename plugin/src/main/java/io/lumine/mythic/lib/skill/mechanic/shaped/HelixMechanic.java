package io.lumine.mythic.lib.skill.mechanic.shaped;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.Mechanic;
import io.lumine.mythic.lib.skill.targeter.LocationTargeter;
import io.lumine.mythic.lib.skill.targeter.location.ConstantLocationTargeter;
import io.lumine.mythic.lib.skill.targeter.location.DefaultLocationTargeter;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Performs what looks like a sword slash in
 * front of the caster/target entity/target location.
 */
public class HelixMechanic extends Mechanic {
    private final DoubleFormula radius;
    private final double yawSpread, height;
    private final long points, timeInterval, pointsPerTick;
    private final LocationTargeter direction, targetLocation;

    private final Skill onTick, onEnd;

    public HelixMechanic(ConfigObject config) {
        targetLocation = config.contains("source") ? MythicLib.plugin.getSkills().loadLocationTargeter(config.getObject("source")) : new DefaultLocationTargeter();
        direction = config.contains("direction") ? MythicLib.plugin.getSkills().loadLocationTargeter(config.getObject("direction")) : new ConstantLocationTargeter(1, 0, 0);

        config.validateKeys("tick");

        onTick = MythicLib.plugin.getSkills().getSkillOrThrow(config.getString("tick"));
        onEnd = config.contains("end") ? MythicLib.plugin.getSkills().getSkillOrThrow(config.getString("end")) : null;

        yawSpread = config.getDouble("yaw", 360);
        height = config.getDouble("pitch", 3);
        radius = config.contains("radius") ? new DoubleFormula(config.getString("radius")) : new DoubleFormula(3);

        points = config.getInteger("points", 40);
        timeInterval = config.getInteger("time_interval", 1);
        pointsPerTick = config.getInteger("points_per_tick", 3);

        Validate.isTrue(yawSpread > 0, "Yaw spread must be strictly positive");
        Validate.isTrue(height > 0, "Height must be strictly positive");
        Validate.isTrue(points > 0, "Points must be strictly positive");
        Validate.isTrue(timeInterval > 0, "Time interval must be strictly positive");
        Validate.isTrue(pointsPerTick > 0, "Points per tick must be strictly positive");
    }

    @Override
    public void cast(SkillMetadata meta) {

        // This better not be empty
        Vector dir = direction.findTargets(meta).get(0).toVector();

        for (Location loc : targetLocation.findTargets(meta))
            cast(meta, loc, dir);
    }

    public void cast(SkillMetadata meta, Location source, Vector dir) {
        Validate.isTrue(dir.lengthSquared() > 0, "Direction cannot be zero");

        double[] yawPitch = UtilityMethods.getYawPitch(dir);
        double yaw_i = yawPitch[0] - yawSpread / 2;

        new BukkitRunnable() {

            // Tick counter
            int counter = 0;

            public void run() {
                for (int i = 0; i < pointsPerTick; i++) {
                    if (counter++ >= points) {
                        cancel();
                        if (onEnd != null)
                            onEnd.cast(meta);
                        return;
                    }

                    Location loc = source.clone();
                    double yaw = Math.toRadians(yaw_i + ((double) counter / points) * yawSpread);
                    double y = ((double) counter / points) * height;
                    double r = radius.evaluate(meta);
                    loc.add(r * Math.cos(yaw),
                            y,
                            r * Math.sin(yaw));

                    onTick.cast(meta.clone(source, loc, null));
                }
            }
        }.runTaskTimer(MythicLib.plugin, 0, timeInterval);
    }
}
