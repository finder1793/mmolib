package io.lumine.mythic.lib.listener.option;


import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.comp.holograms.HologramSupport;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.text.DecimalFormat;

public class DamageIndicators implements Listener {
    private final HologramSupport holo;
    private final String format;
    private final DecimalFormat decFormat;

    public DamageIndicators(ConfigurationSection config) {
        decFormat = new DecimalFormat(config.getString("decimal-format"));
        holo = MythicLib.plugin.getHologramSupport();
        format = config.getString("format");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void a(EntityDamageEvent event) {

        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity) || event.getEntity() instanceof ArmorStand || event.getDamage() <= 0)
            return;

        /*
         * No damage indicator is displayed when
         * the player is vanished using Essentials.
         */
        if (entity instanceof Player && holo.isVanished((Player) entity))
            return;

        holo.displayIndicator(entity, format.replace("#", decFormat.format(event.getFinalDamage())));
    }
}
