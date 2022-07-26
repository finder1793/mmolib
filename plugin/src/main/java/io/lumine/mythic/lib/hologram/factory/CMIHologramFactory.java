package io.lumine.mythic.lib.hologram.factory;

import com.Zrips.CMI.CMI;
import io.lumine.mythic.lib.hologram.Hologram;
import io.lumine.mythic.lib.hologram.HologramFactory;
import net.Zrips.CMILib.Container.CMILocation;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;

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

    @Override
    public Hologram newHologram(Location loc, List<String> lines) {
        return new CMIHologram(loc, lines);
    }

    public class CMIHologram implements Hologram {
        private final com.Zrips.CMI.Modules.Holograms.CMIHologram holo;
        private boolean spawned = true;

        public CMIHologram(Location loc, List<String> list) {
            holo = new com.Zrips.CMI.Modules.Holograms.CMIHologram("MythicLib-" + UUID.randomUUID().toString(), new CMILocation(loc));
            holo.setLines(list);

            CMI.getInstance().getHologramManager().addHologram(holo);
            holo.update();
        }

        @Override
        public void updateLines(List<String> list) {
            holo.setLines(list);
            holo.update();
        }

        @Override
        public Location getLocation() {
            return holo.getLocation();
        }

        @Override
        public void despawn() {
            Validate.isTrue(spawned, "Hologram is already despawned");
            CMI.getInstance().getHologramManager().removeHolo(holo);
            spawned = false;
        }

        @Override
        public boolean isSpawned() {
            return spawned;
        }

        @Override
        public void updateLocation(Location loc) {
            holo.setLoc(loc);
            holo.update();
        }

        @Override
        public List<String> getLines() {
            return holo.getLines();
        }
    }
}
