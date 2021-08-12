package io.lumine.mythic.lib.comp.hologram.factory;

import io.lumine.mythic.lib.comp.hologram.MMOItemsHologram;
import io.lumine.utils.holograms.Hologram;
import io.lumine.utils.holograms.HologramFactory;
import io.lumine.utils.serialize.Position;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.api.hologram.HologramBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Handles compatibility with TrHologram using HologramFactory from LumineUtils.
 *
 * @author TUCAOEVER, indyuce
 */
public class TrHologramFactory implements HologramFactory {

    public void displayIndicator(final Location loc, final String format, final Player player) {
//        Hologram hologram = TrHologramAPI.builder(loc)
//                .append(format)
//                .build();
//        Bukkit.getScheduler().scheduleSyncDelayedTask(MythicLib.plugin, hologram::destroy, 20L);
    }

    @NotNull
    @Override
    public Hologram newHologram(@NotNull Position position, @NotNull List<String> list) {
        return new TrHologram(position, list);
    }

    public class TrHologram extends MMOItemsHologram {
        private final me.arasple.mc.trhologram.module.display.Hologram holo;
        private final List<String> lines;

        public TrHologram(Position position, List<String> list) {
            HologramBuilder builder = TrHologramAPI.builder(position.toLocation());
            for (String line : list)
                builder.append(line);
            holo = builder.build();
            this.lines = list;
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
        public Position getPosition() {
            return Position.of(holo.getPosition().toLocation());
        }

        @Override
        public void updatePosition(@NotNull Position position) {
            // Not supported
        }

        @Override
        public void despawn() {
            super.despawn();

            holo.destroy();
        }
    }
}
