package io.lumine.mythic.lib.script.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.mechanic.shaped.HelixMechanic;
import io.lumine.mythic.lib.script.targeter.LocationTargeter;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Used by {@link HelixMechanic} to provide a default direction when none is given
 *
 * This is never used by players and is used only internally
 */
public class ConstantLocationTargeter extends LocationTargeter {
    private final double x, y, z;

    public ConstantLocationTargeter(double x, double y, double z) {
        super(false);

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(new Location(meta.getSourceLocation().getWorld(), x, y, z));
    }
}
