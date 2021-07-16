package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.gui.PluginInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void loadPlayerData(AsyncPlayerPreLoginEvent event) {
        MMOPlayerData.setup(event.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void runStatUpdatesOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MMOPlayerData data = MMOPlayerData.get(player);

        // Update cached player instance
        data.updatePlayer(player);

        // Run stat updates on login
        MythicLib.plugin.getStats().runUpdates(data.getStatMap());
    }

    @EventHandler
    public void registerOfflinePlayers(PlayerQuitEvent event) {

        /**
         * See {@link MMOPlayerData#isOnline()}
         */
        MMOPlayerData.get(event.getPlayer()).updatePlayer(null);
    }

    @EventHandler
    public void handleCustomInventoryClicks(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof PluginInventory)
            ((PluginInventory) event.getInventory().getHolder()).whenClicked(event);
    }
}
