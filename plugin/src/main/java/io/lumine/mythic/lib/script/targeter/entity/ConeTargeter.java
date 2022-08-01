package io.lumine.mythic.lib.script.targeter.entity;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.targeter.EntityTargeter;
import io.lumine.mythic.lib.script.targeter.LocationTargeter;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ConeTargeter implements EntityTargeter {
    private final DoubleFormula radius, angle;
    private final LocationTargeter sourceLocation, direction;

    public ConeTargeter(ConfigObject config) {
        config.validateKeys("radius", "angle");

        sourceLocation = config.contains("source") ? MythicLib.plugin.getSkills().loadLocationTargeter(config.getObject("source")) : null;
        direction = config.contains("direction") ? MythicLib.plugin.getSkills().loadLocationTargeter(config.getObject("direction")) : null;

        angle = new DoubleFormula(config.getString("angle"));
        radius = new DoubleFormula(config.getString("radius"));
    }

    @Override
    public List<Entity> findTargets(SkillMetadata meta) {

        Location loc = sourceLocation == null ? meta.getCaster().getPlayer().getEyeLocation() : sourceLocation.findTargets(meta).get(0);
        Vector dir = direction == null ? loc.getDirection() : direction.findTargets(meta).get(0).toVector();

        double rad = radius.evaluate(meta);
        double angle = Math.toRadians(this.angle.evaluate(meta));

        List<Entity> list = new ArrayList<>();
        for (Entity nearby : loc.getWorld().getNearbyEntities(loc, rad, rad, rad))
            if (nearby.getLocation().subtract(loc).toVector().angle(dir) < angle && !nearby.equals(meta.getCaster().getPlayer()))
                list.add(nearby);

        return list;
    }
}
