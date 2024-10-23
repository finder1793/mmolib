package io.lumine.mythic.lib.hologram;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface HologramFactory {

    /**
     * Creates and spawns a hologram at given location with
     * given lines.
     *
     * @param loc   Target hologram location
     * @param lines Messages to display. Multiple lines are supported
     */
    @NotNull
    Hologram newHologram(@NotNull Location loc, @NotNull List<String> lines);
}
