package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.util.LegacyComponent;
import io.lumine.mythic.lib.gui.PluginInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    @EventHandler(priority = EventPriority.MONITOR)
    public void registerOfflinePlayers(PlayerQuitEvent event) {

        /**
         * See {@link MMOPlayerData#isOnline()}
         */
        MMOPlayerData.get(event.getPlayer()).updatePlayer(null);
    }

    @EventHandler
    public void test(AsyncPlayerChatEvent event) {

        Bukkit.getScheduler().runTask(MythicLib.plugin, () -> {

            ItemStack item = new ItemStack(Material.DIAMOND);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("Fc");
            item.setItemMeta(meta);

            NBTItem nbt = NBTItem.get(item);
            nbt.setDisplayNameComponent(LegacyComponent.parse(event.getMessage()));
            event.getPlayer().getInventory().addItem(nbt.toItem());

        });

    }

    @EventHandler
    public void handleCustomInventoryClicks(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof PluginInventory)
            ((PluginInventory) event.getInventory().getHolder()).whenClicked(event);
    }
}
