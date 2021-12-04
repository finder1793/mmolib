package io.lumine.mythic.lib.listener.option;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.IndicatorDisplayEvent;
import io.lumine.utils.holograms.Hologram;
import io.lumine.utils.serialize.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

public abstract class GameIndicators implements Listener {
    private final String format;
    private final DecimalFormat decFormat;

    protected static final Random random = new Random();

    /**
     * Hologram life span in ticks
     */
    private static final int HOLOGRAM_LIFE_SPAN = 7;

    public GameIndicators(ConfigurationSection config) {
        decFormat = new DecimalFormat(config.getString("decimal-format"));
        format = config.getString("format");
    }

    public String formatNumber(double d) {
        return decFormat.format(d);
    }

    public String getFormat() {
        return format;
    }

    /**
     * Displays a message using a hologram around an entity.
     * <p>
     * Holograms are provided through LumineUtils, which implements a
     * basic hologram provider when no other plugin is used and a
     * packet-handled hologram system when ProtocolLib is installed.
     *
     * @param entity  Entity used to find the hologram initial position.
     * @param message Message to display
     * @param dir     Average direction of the hologram indicator
     */
    public void displayIndicator(Entity entity, String message, @NotNull Vector dir, IndicatorDisplayEvent.IndicatorType type) {

        IndicatorDisplayEvent called = new IndicatorDisplayEvent(entity, message, type);
        Bukkit.getPluginManager().callEvent(called);
        if (called.isCancelled())
            return;

        Location loc = entity.getLocation().add((random.nextDouble() - .5) * 1.2, entity.getHeight() * .75, (random.nextDouble() - .5) * 1.2);
        displayIndicator(loc, called.getMessage(), dir);
    }

    private void displayIndicator(Location loc, String message, @NotNull Vector dir) {

        // Use individual holo to hide the temporary armor stand
        Hologram holo = Hologram.create(Position.of(loc), Arrays.asList(MythicLib.plugin.parseColors(message)));

        // Parabola trajectory
        new BukkitRunnable() {
            double v = 6; // Initial velocity
            int i = 0; // Counter

            private static final double acc = -10; // Downwards acceleration
            private static final double dt = 3d / 20d; // Delta_t used to integrate acceleration and velocity

            @Override
            public void run() {

                if (i == 0)
                    dir.multiply(2);

                // Remove hologram when reaching end of life
                if (i++ >= HOLOGRAM_LIFE_SPAN) {
                    holo.despawn();
                    cancel();
                    return;
                }

                v += acc * dt;
                loc.add(dir.getX() * dt, v * dt, dir.getZ() * dt);
                holo.updatePosition(Position.of(loc));
            }
        }.runTaskTimer(MythicLib.plugin, 0, 3);
    }

    /**
     * Indicators should not display around vanished players.
     * The 'vanished' meta data should be updated by vanish plugins
     * to let all the plugins knows when a player is vanished.
     *
     * @return If a given player can display holograms around him
     */
    public boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished"))
            if (meta.asBoolean())
                return true;
        return false;
    }
}
