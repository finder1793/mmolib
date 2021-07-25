package io.lumine.mythic.lib.listener.option;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.comp.holograms.HologramSupport;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.text.DecimalFormat;

public class RegenIndicators implements Listener {
    private final HologramSupport holo;
    private final String format;
    private final DecimalFormat decFormat;

    public RegenIndicators(ConfigurationSection config) {
        decFormat = new DecimalFormat(config.getString("decimal-format"));
        holo = MythicLib.plugin.getHologramSupport();
        format = config.getString("format");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void a(EntityRegainHealthEvent event) {

        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity) || event.getAmount() <= 0
                || ((LivingEntity) entity).getHealth() >= ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
            return;

        /*
         * No damage indicator is displayed when
         * the player is vanished using Essentials.
         */
        if (entity instanceof Player && holo.isVanished((Player) entity))
            return;

        holo.displayIndicator(entity, format.replace("#", decFormat.format(event.getAmount())));
    }
}
