package io.lumine.mythic.lib.skill.targeter.entity;

import io.lumine.mythic.lib.util.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.targeter.EntityTargeter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class NearbyEntitiesTargeter implements EntityTargeter {
    private final DoubleFormula radius;
    private final boolean source;

    public NearbyEntitiesTargeter(ConfigObject config) {
        config.validateKeys("radius");

        source = config.getBoolean("source", false);
        radius = new DoubleFormula(config.getString("radius"));
    }

    @Override
    public List<Entity> findTargets(SkillMetadata meta) {
        Location loc = meta.getSkillLocation(source);
        double rad = radius.evaluate(meta);
        return new ArrayList<>(loc.getWorld().getNearbyEntities(loc, rad, rad, rad));
    }
}
