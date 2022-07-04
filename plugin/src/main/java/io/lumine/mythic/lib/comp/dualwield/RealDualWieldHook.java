package io.lumine.mythic.lib.comp.dualwield;

import com.evill4mer.RealDualWield.Api.PlayerDamageEntityWithOffhandEvent;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RealDualWieldHook implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void a(PlayerDamageEntityWithOffhandEvent event) {
        MythicLib.plugin.getDamage().registerOffHandAttack(event.getEntity());
    }
}
