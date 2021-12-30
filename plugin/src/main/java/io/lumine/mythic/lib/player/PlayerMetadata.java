package io.lumine.mythic.lib.player;

import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * A class containing the information about a player that can
 * be used to temporarily cache its statistics for instance
 * when attacking or casting a skill
 */
public abstract class PlayerMetadata {
    private final StatMap.CachedStatMap statMap;

    public PlayerMetadata(StatMap.CachedStatMap statMap) {
        this.statMap = Objects.requireNonNull(statMap, "Stat map cannot be null");
    }

    public StatMap.CachedStatMap getStats() {
        return statMap;
    }

    public Player getPlayer() {
        return statMap.getPlayer();
    }
}
