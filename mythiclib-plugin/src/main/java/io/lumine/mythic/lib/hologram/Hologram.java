package io.lumine.mythic.lib.hologram;

import org.bukkit.Bukkit;
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
        return Bukkit.getServicesManager().getRegistration(HologramFactory.class).getProvider().newHologram(loc, lines);
    }
}
