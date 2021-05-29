package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerLoadEvent(AsyncPlayerPreLoginEvent e) {
        MMOPlayerData.setup(e.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoinEvent(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        if (MMOPlayerData.isLoaded(uuid))
            MythicLib.plugin.getStats().runUpdates(MMOPlayerData.get(uuid).getStatMap());
    }

    @EventHandler
    public void b(PlayerQuitEvent event) {
        MMOPlayerData.get(event.getPlayer()).setPlayer(null);
    }
}
