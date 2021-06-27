package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.gui.PluginInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void loadPlayerData(AsyncPlayerPreLoginEvent e) {
        MMOPlayerData.setup(e.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void runStatUpdatesOnJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        if (MMOPlayerData.isLoaded(uuid))
            MythicLib.plugin.getStats().runUpdates(MMOPlayerData.get(uuid).getStatMap());
    }

    @EventHandler
    public void registerOfflinePlayers(PlayerQuitEvent event) {

        /**
         * See {@link MMOPlayerData#isOnline()}
         */
        MMOPlayerData.get(event.getPlayer()).setPlayer(null);
    }

    @EventHandler
    public void handleCustomInventoryClicks(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof PluginInventory)
            ((PluginInventory) event.getInventory().getHolder()).whenClicked(event);
    }
}
