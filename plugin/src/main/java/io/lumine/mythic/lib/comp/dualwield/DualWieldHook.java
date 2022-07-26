package io.lumine.mythic.lib.comp.dualwield;

import com.ranull.dualwield.event .OffHandAttackEvent;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DualWieldHook implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void a(OffHandAttackEvent event) {
        MythicLib.plugin.getDamage().registerOffHandAttack(event.getEntity());
    }
}
