package io.lumine.mythic.lib.comp.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.Random;

public abstract class HologramSupport {
    private static final Random random = new Random();

    /**
     * Displays a hologram randomly around the target. Used
     * for damage or regen indicators.
     *
     * @param entity  Entity hit or regenerating
     * @param message Message being displayed
     */
    public void displayIndicator(Entity entity, String message) {
        displayIndicator(entity.getLocation().add((random.nextDouble() - .5) * 1.2, entity.getHeight() * .75, (random.nextDouble() - .5) * 1.2),
                message, entity instanceof Player ? (Player) entity : null);
    }

    /**
     * Displays a message using a hologram indicator
     *
     * @param loc     Loation to spawn the hologram at
     * @param message Displayed message
     * @param player  Player spawning the hologram. The hologram, if possible,
     *                should be hidden from that player
     */
    public abstract void displayIndicator(Location loc, String message, Player player);

    public boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished"))
            if (meta.asBoolean())
                return true;
        return false;
    }
}
