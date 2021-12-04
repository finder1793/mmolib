package io.lumine.mythic.lib.api.condition.type;

import org.bukkit.Location;
import org.bukkit.World;

public interface WorldCondition extends LocationCondition {
    boolean check(World world);

    @Override
    default boolean check(Location location) {
        return check(location.getWorld());
    }
}