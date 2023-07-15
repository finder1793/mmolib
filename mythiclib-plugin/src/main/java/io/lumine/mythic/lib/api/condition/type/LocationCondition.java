package io.lumine.mythic.lib.api.condition.type;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface LocationCondition extends PlayerCondition, BlockCondition {
    boolean check(Location location);

    @Override
    default boolean check(Player player) {
        return check(player.getLocation());
    }

    @Override
    default boolean check(Block block) {
        return check(block.getLocation());
    }
}
