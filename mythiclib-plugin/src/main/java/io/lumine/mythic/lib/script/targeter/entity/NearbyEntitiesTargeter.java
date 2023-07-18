package io.lumine.mythic.lib.script.targeter.entity;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.targeter.EntityTargeter;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class NearbyEntitiesTargeter implements EntityTargeter {
    private final DoubleFormula radius, height;
    private final boolean source;

    public NearbyEntitiesTargeter(ConfigObject config) {
        config.validateKeys("radius");

        source = config.getBoolean("source", false);
        radius = new DoubleFormula(config.getString("radius"));
        height = config.contains("height") ? new DoubleFormula(config.getString("height")) : null;
    }

    @Override
    public List<Entity> findTargets(SkillMetadata meta) {
        Location loc = meta.getSkillLocation(source);
        final double rad = radius.evaluate(meta), height = this.height == null ? rad : this.height.evaluate(meta);
        return new ArrayList<>(loc.getWorld().getNearbyEntities(loc, rad, height, rad));
    }
}
