package io.lumine.mythic.lib.hologram;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Location;

import java.util.List;

public interface Hologram {

    void despawn();

    boolean isSpawned();

    void updateLocation(Location loc);

    void updateLines(List<String> lines);

    List<String> getLines();

    Location getLocation();

    static Hologram create(Location loc, List<String> lines) {
        return MythicLib.plugin.getHologramFactory().newHologram(loc, lines);
    }
}
