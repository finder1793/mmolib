package io.lumine.mythic.lib.comp.hologram.factory;

import com.Zrips.CMI.CMI;
import io.lumine.mythic.lib.comp.hologram.MMOHologram;
import io.lumine.utils.holograms.Hologram;
import io.lumine.utils.holograms.HologramFactory;
import io.lumine.utils.serialize.Position;
import net.Zrips.CMILib.Container.CMILocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Handles compatibility with CMI using HologramFactory from LumineUtils.
 * <p>
 * That being said, CMI has a bad implementation of holograms
 * and therefore has a lower service priority registered: dedicated
 * plugins like HolographicDisplays or Holograms should be prioritized.
 *
 * @author indyuce
 */
public class CMIHologramFactory implements HologramFactory {

    /*public void displayIndicator(Location loc, String format, Player player) {
        final com.Zrips.CMI.Modules.Holograms.CMIHologram hologram = new com.Zrips.CMI.Modules.Holograms.CMIHologram("MythicLib_" + UUID.randomUUID().toString(), loc);
        hologram.setLines(Collections.singletonList(format));
        if (player != null)
            hologram.hide(player.getUniqueId());
        CMI.getInstance().getHologramManager().addHologram(hologram);
        hologram.update();

        Bukkit.getScheduler().scheduleSyncDelayedTask(MythicLib.plugin, () -> CMI.getInstance().getHologramManager().removeHolo(hologram), 20);
    }*/

    @NotNull
    @Override
    public Hologram newHologram(@NotNull Position position, @NotNull List<String> list) {
        return new CMIHologram(position, list);
    }

    public class CMIHologram extends MMOHologram {
        private final com.Zrips.CMI.Modules.Holograms.CMIHologram holo;

        public CMIHologram(Position position, List<String> list) {
            holo = new com.Zrips.CMI.Modules.Holograms.CMIHologram("MythicLib-" + UUID.randomUUID().toString(), new CMILocation(position.toLocation()));
            holo.setLines(list);
        }

        @Override
        public void spawn() {
            CMI.getInstance().getHologramManager().addHologram(holo);
            holo.update();
        }

        @Override
        public void updateLines(@NotNull List<String> list) {
            holo.setLines(list);
            holo.update();
        }

        @Override
        public Position getPosition() {
            return Position.of(holo.getLocation());
        }

        @Override
        public void despawn() {
            super.despawn();

            CMI.getInstance().getHologramManager().removeHolo(holo);
        }

        @Override
        public void updatePosition(@NotNull Position position) {
            holo.setLoc(position.toLocation());
        }

        @Override
        public List<String> getLines() {
            return holo.getLines();
        }
    }
}
