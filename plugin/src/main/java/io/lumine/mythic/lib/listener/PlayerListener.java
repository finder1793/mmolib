package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.TemporaryDataSavedEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.gui.PluginInventory;
import io.lumine.mythic.lib.player.TemporaryPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    /**
     * Async pre join events are unreliable for some reason so
     * it seems to be better to initialize player data on the
     * lowest priority possible on sync when the player joins.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void loadData(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MMOPlayerData data = MMOPlayerData.setup(player);

        // Run stat updates on login
        MythicLib.plugin.getStats().runUpdates(data.getStatMap());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void loggingOff(PlayerQuitEvent event) {
        TemporaryDataSavedEvent called = new TemporaryDataSavedEvent(MMOPlayerData.get(event.getPlayer()));
        TemporaryPlayerData.load(event.getPlayer(), called.getTemporaryData());
        Bukkit.getPluginManager().callEvent(called);

        // TODO unload MMOPlayerData bc of memory leaks. May require a lot of tests & small changes tho
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void loggingOn(PlayerJoinEvent event) {
        TemporaryPlayerData.unload(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
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
