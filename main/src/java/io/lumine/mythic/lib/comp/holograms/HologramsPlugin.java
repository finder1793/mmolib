package io.lumine.mythic.lib.comp.holograms;

import com.sainttx.holograms.HologramPlugin;
import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramManager;
import com.sainttx.holograms.api.line.TextLine;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class HologramsPlugin extends HologramSupport {
    private final HologramManager hologramManager = JavaPlugin.getPlugin(HologramPlugin.class).getHologramManager();

    @Override
    public void displayIndicator(Location loc, String message, Player player) {
        Hologram hologram = new Hologram("MythicLib_" + UUID.randomUUID().toString(), loc);
        hologramManager.addActiveHologram(hologram);
        hologram.addLine(new TextLine(hologram, message));
        Bukkit.getScheduler().scheduleSyncDelayedTask(MythicLib.plugin, () -> hologramManager.deleteHologram(hologram), 20);
    }
}
