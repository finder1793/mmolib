package io.lumine.mythic.lib.skill.custom.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.targeter.LocationTargeter;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Provides a location
 */
public class CustomLocationTargeter implements LocationTargeter {
    private final DoubleFormula x, y,z;

    public CustomLocationTargeter(ConfigObject config) {
        config.validateKeys("x", "y", "z");

        this.x = new DoubleFormula(config.getString("x"));
        this.y = new DoubleFormula(config.getString("y"));
        this.z = new DoubleFormula(config.getString("z"));
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(new Location(meta.getSourceLocation().getWorld(), x.evaluate(meta), y.evaluate(meta), z.evaluate(meta)));
    }
}
