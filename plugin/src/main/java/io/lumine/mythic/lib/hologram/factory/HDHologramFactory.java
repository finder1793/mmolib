package io.lumine.mythic.lib.hologram.factory;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.hologram.Hologram;
import io.lumine.mythic.lib.hologram.HologramFactory;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Handles compatibility with HolographicDisplays using HologramFactory from LumineUtils.
 * <p>
 * HolographicDisplays is by far the best hologram plugin we
 * can use to display indicators.
 *
 * @author indyuce
 */
public class HDHologramFactory implements HologramFactory {


   /* public void displayIndicator(Location loc, String format, Player player) {
        Hologram hologram = HologramsAPI.createHologram(MythicLib.plugin, loc);
        hologram.appendTextLine(format);
        if (player != null)
            hologram.getVisibilityManager().hideTo(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(MythicLib.plugin, hologram::delete, 20);
    }*/

    @Override
    public Hologram newHologram(Location loc, List<String> lines) {

        return new HDHologram(loc, lines);
    }

    public static class HDHologram implements Hologram {
        private final me.filoghost.holographicdisplays.api.hologram.@NotNull Hologram holo;
        private final List<String> lines;
        private boolean spawned = true;

        public HDHologram(Location loc, List<String> list) {
            this.lines = list;
            this.holo = HolographicDisplaysAPI.get(MythicLib.plugin).createHologram(loc);
            for (String line : lines)
                this.holo.getLines().appendText(line);
        }

        @Override
        public List<String> getLines() {
            return lines;
        }

        @Override
        public void updateLines(@NotNull List<String> list) {
            throw new NotImplementedException("Adding lines is not supported");
        }

        @Override
        public Location getLocation() {
            return holo.getPosition().toLocation();
        }

        @Override
        public void updateLocation(Location loc) {
            holo.setPosition(loc);
        }

        @Override
        public void despawn() {
            Validate.isTrue(spawned, "Hologram is already despawned");
            holo.delete();
            spawned = false;
        }

        @Override
        public boolean isSpawned() {
            return spawned;
        }
    }
}
