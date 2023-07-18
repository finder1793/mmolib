package io.lumine.mythic.lib.hologram.factory;

import io.lumine.mythic.lib.hologram.Hologram;
import io.lumine.mythic.lib.hologram.HologramFactory;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.api.hologram.HologramBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Handles compatibility with TrHologram using HologramFactory from LumineUtils.
 *
 * @author TUCAOEVER, indyuce
 */
public class TrHologramFactory implements HologramFactory {

  /*  public void displayIndicator(final Location loc, final String format, final Player player) {
        Hologram hologram = TrHologramAPI.builder(loc)
                .append(format)
                .build();
        Bukkit.getScheduler().scheduleSyncDelayedTask(MythicLib.plugin, hologram::destroy, 20L);
    }*/

    @Override
    public Hologram newHologram(Location loc, List<String> lines) {
        return new TrHologram(loc, lines);
    }

    public class TrHologram implements Hologram {
        private final me.arasple.mc.trhologram.module.display.Hologram holo;
        private final List<String> lines;
        private boolean spawned = true;

        public TrHologram(Location loc, List<String> lines) {
            HologramBuilder builder = TrHologramAPI.builder(loc);
            for (String line : lines)
                builder.append(line);
            holo = builder.build();
            this.lines = lines;
        }

        @Override
        public boolean isSpawned() {
            return spawned;
        }

        @Override
        public List<String> getLines() {
            return lines;
        }

        @Override
        public void updateLines(@NotNull List<String> list) {
            throw new RuntimeException("Updating lines is not supported");
        }

        @Override
        public Location getLocation() {
            return holo.getPosition().toLocation();
        }

        @Override
        public void updateLocation(Location loc) {
            // Not supported
        }

        @Override
        public void despawn() {
            Validate.isTrue(spawned, "Hologram is already despawned");
            holo.destroy();
            spawned = false;
        }
    }
}
