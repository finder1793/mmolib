package io.lumine.mythic.lib.comp.holograms;

import io.lumine.utils.Schedulers;
import io.lumine.utils.holograms.Hologram;
import io.lumine.utils.serialize.Position;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MythicLibHologramSupport extends HologramSupport {
    @Override
    public void displayIndicator(Location loc, String message, Player player) {
        Hologram h = Hologram.create(Position.of(loc), Collections.singletonList(message));
        h.spawn();

        Schedulers.sync().runLater(() -> {
            h.despawn();
            h.terminate();
        }, 20);
    }
}
