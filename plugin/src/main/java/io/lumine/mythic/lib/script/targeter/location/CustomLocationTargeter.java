package io.lumine.mythic.lib.script.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.targeter.LocationTargeter;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Provides a location which coordinates are provided as parameters
 */
@Orientable
public class CustomLocationTargeter extends LocationTargeter {
    private final DoubleFormula x, y, z;
    private final boolean relative, source;

    public CustomLocationTargeter(ConfigObject config) {
        super(config);

        config.validateKeys("x", "y", "z");

        this.x = new DoubleFormula(config.getString("x"));
        this.y = new DoubleFormula(config.getString("y"));
        this.z = new DoubleFormula(config.getString("z"));

        relative = config.getBoolean("relative", true);
        source = config.getBoolean("source", false);
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        final Location loc = relative ? meta.getSkillLocation(source) : new Location(meta.getSourceLocation().getWorld(), 0, 0, 0);
        loc.add(x.evaluate(meta), y.evaluate(meta), z.evaluate(meta));
        return Arrays.asList(loc);
    }
}
