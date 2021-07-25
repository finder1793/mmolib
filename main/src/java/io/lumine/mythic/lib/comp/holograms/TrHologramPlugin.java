package io.lumine.mythic.lib.comp.holograms;

import io.lumine.mythic.lib.MythicLib;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.module.display.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Updated comp provided through discord
 *
 * @author TUCAOEVER
 */
public class TrHologramPlugin extends HologramSupport {

    @Override
    public void displayIndicator(final Location loc, final String format, final Player player) {
        Hologram hologram = TrHologramAPI.builder(loc)
                .append(format)
                .build();
        Bukkit.getScheduler().scheduleSyncDelayedTask(MythicLib.plugin, hologram::destroy, 20L);
    }
}
