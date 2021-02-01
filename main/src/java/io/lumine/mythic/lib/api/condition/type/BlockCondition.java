package io.lumine.mythic.lib.api.condition.type;

import org.bukkit.block.Block;

public interface BlockCondition {
    boolean check(Block block);
}
