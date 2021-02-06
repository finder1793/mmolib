package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerLoadEvent(AsyncPlayerPreLoginEvent e) {
        MMOPlayerData.setup(e.getUniqueId());
    }

    @EventHandler
    public void b(PlayerQuitEvent event) {
        MMOPlayerData.get(event.getPlayer()).setPlayer(null);
    }


}
