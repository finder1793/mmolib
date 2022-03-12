package io.lumine.mythic.lib.comp.mythicmobs;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MythicMobsHook implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void a(PlayerAttackEvent event) {

        // Apply MythicMobs faction damage
        String faction = getFaction(event.getEntity());
        if (faction != null)
            event.getDamage().additiveModifier(event.getData().getStatMap().getStat("FACTION_DAMAGE_" + faction.toUpperCase()) / 100);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void b(MythicReloadedEvent event) {

        // Reload skills
        MythicLib.plugin.getSkills().initialize(true);
    }

    private String getFaction(Entity entity) {
        ActiveMob mob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
        return mob != null && mob.hasFaction() ? mob.getFaction() : null;
    }
}
