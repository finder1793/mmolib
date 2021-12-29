package io.lumine.mythic.lib.skill.custom.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.shaped.HelixMechanic;
import io.lumine.mythic.lib.skill.custom.targeter.LocationTargeter;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Used by {@link HelixMechanic} to provide a default direction when none is given
 */
public class ConstantLocationTargeter implements LocationTargeter {
    private final double x, y, z;

    public ConstantLocationTargeter(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(new Location(meta.getSourceLocation().getWorld(), x, y, z));
    }
}
