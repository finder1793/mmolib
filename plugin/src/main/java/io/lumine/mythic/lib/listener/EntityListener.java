package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EntityListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void unregisterProjectileData(ProjectileHitEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(MythicLib.plugin, () -> MythicLib.plugin.getEntities().unregisterCustomProjectile(event.getEntity()));
    }
}
