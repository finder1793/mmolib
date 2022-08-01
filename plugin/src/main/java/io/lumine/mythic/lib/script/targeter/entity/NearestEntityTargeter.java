package io.lumine.mythic.lib.script.targeter.entity;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.targeter.EntityTargeter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class NearestEntityTargeter implements EntityTargeter {
    private final DoubleFormula radius;
    private final boolean source;

    public NearestEntityTargeter(ConfigObject config) {
        config.validateKeys("radius");

        source = config.getBoolean("source", false);
        radius = new DoubleFormula(config.getString("radius"));
    }

    @Override
    public List<Entity> findTargets(SkillMetadata meta) {
        Location loc = meta.getSkillLocation(source);
        double rad = radius.evaluate(meta);

        Entity nearest = null;
        double dist = Double.MAX_VALUE;

        for (Entity entity : loc.getWorld().getNearbyEntities(loc, rad, rad, rad)) {
            double checked = entity.getLocation().distanceSquared(loc);
            if (checked < dist) {
                nearest = entity;
                dist = checked;
            }
        }

        List<Entity> list = new ArrayList();
        if (nearest != null)
            list.add(nearest);
        return list;
    }
}
