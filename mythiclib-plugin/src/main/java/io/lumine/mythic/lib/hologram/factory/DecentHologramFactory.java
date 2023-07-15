package io.lumine.mythic.lib.hologram.factory;

import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import io.lumine.mythic.lib.hologram.Hologram;
import io.lumine.mythic.lib.hologram.HologramFactory;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DecentHologramFactory implements HologramFactory {

    @Override
    public Hologram newHologram(Location loc, List<String> lines) {
        return new CustomHologram(loc, lines);
    }

    public class CustomHologram implements Hologram {
        private final eu.decentsoftware.holograms.api.holograms.Hologram holo;
        private final HologramPage page;
        private boolean spawned = true;

        public CustomHologram(Location loc, List<String> lines) {
            holo = new eu.decentsoftware.holograms.api.holograms.Hologram(UUID.randomUUID().toString(), loc, false);
            page = holo.getPage(0);
            for (String line : lines)
                page.addLine(new HologramLine(page, page.getNextLineLocation(), line));

            holo.showAll();
        }

        @Override
        public void updateLines(List<String> list) {

            // Empty
            while (!page.getLines().isEmpty())
                page.removeLine(0);

            // Add lines again
            for (String line : list)
                page.addLine(new HologramLine(page, page.getNextLineLocation(), line));

            holo.updateAll();
        }

        @Override
        public Location getLocation() {
            return holo.getLocation();
        }

        @Override
        public void despawn() {
            Validate.isTrue(spawned, "Hologram is already despawned");
            holo.destroy();
            spawned = false;
        }

        @Override
        public boolean isSpawned() {
            return spawned;
        }

        @Override
        public void updateLocation(Location loc) {
            holo.setLocation(loc);
            holo.realignLines();
        }

        @Override
        public List<String> getLines() {
            return holo.getPage(0).getLines().stream().map(line -> line.getContent()).collect(Collectors.toList());
        }
    }
}
