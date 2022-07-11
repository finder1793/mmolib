package io.lumine.mythic.lib.hologram;

import org.bukkit.Location;

import java.util.List;

public interface HologramFactory {

    /**
     * Creates and spawns a hologram at given location with
     * given lines.
     *
     * @param loc   Target hologram location
     * @param lines Messages to display. Multiple lines are supported
     */
    Hologram newHologram(Location loc, List<String> lines);
}
