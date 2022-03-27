package io.lumine.mythic.lib.comp.hologram.factory;

import com.sainttx.holograms.HologramPlugin;
import com.sainttx.holograms.api.HologramManager;
import com.sainttx.holograms.api.line.TextLine;
import io.lumine.mythic.lib.comp.hologram.MMOHologram;
import io.lumine.utils.holograms.Hologram;
import io.lumine.utils.holograms.HologramFactory;
import io.lumine.utils.serialize.Position;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handles compatibility with Holograms using HologramFactory from LumineUtils.
 *
 * @author indyuce
 */
public class HologramsHologramFactory implements HologramFactory {
    private final HologramManager hologramManager = JavaPlugin.getPlugin(HologramPlugin.class).getHologramManager();

    /*public void displayIndicator(Location loc, String message, Player player) {
		Hologram hologram = new Hologram("MMOItems_" + UUID.randomUUID().toString(), loc);
		hologramManager.addActiveHologram(hologram);
		hologram.addLine(new TextLine(hologram, message));
		Bukkit.getScheduler().scheduleSyncDelayedTask(MMOItems.plugin, () -> hologramManager.deleteHologram(hologram), 20);
	} */

    @NotNull
    @Override
    public Hologram newHologram(@NotNull Position position, @NotNull List<String> list) {
        return new CustomHologram(position, list);
    }

    public class CustomHologram extends MMOHologram {
        private final com.sainttx.holograms.api.Hologram holo;

        public CustomHologram(Position position, List<String> list) {
            holo = new com.sainttx.holograms.api.Hologram("MythicLib-" + UUID.randomUUID().toString(), position.toLocation());
            for (String str : list)
                holo.addLine(new TextLine(holo, str));
        }

        @Override
        public void spawn() {
            // Spawns on instanciation
        }

        @Override
        public List<String> getLines() {
            return holo.getLines().stream().map(line -> line.getRaw()).collect(Collectors.toList());
        }

        @Override
        public void updateLines(@NotNull List<String> list) {
            throw new RuntimeException("Adding lines is not supported");
        }

        @Override
        public Position getPosition() {
            return Position.of(holo.getLocation());
        }

        @Override
        public void updatePosition(@NotNull Position position) {
            holo.teleport(position.toLocation());
        }

        @Override
        public void despawn() {
            super.despawn();

            hologramManager.deleteHologram(holo);
        }
    }
}
