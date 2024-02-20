package io.lumine.mythic.lib.data;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerProvider {
    public Player getPlayer();
}
