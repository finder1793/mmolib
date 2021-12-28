package io.lumine.mythic.lib.listener.option;


import io.lumine.mythic.lib.api.event.IndicatorDisplayEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class DamageIndicators extends GameIndicators {
    public DamageIndicators(ConfigurationSection config) {
        super(config);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void a(EntityDamageEvent event) {

        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity) || event.getEntity() instanceof ArmorStand || event.getDamage() <= 0)
            return;

        // Display no indicator around vanished player
        if (entity instanceof Player && isVanished((Player) entity))
            return;

        displayIndicator(entity, getFormat().replace("#", formatNumber(event.getFinalDamage())), getDirection(event), IndicatorDisplayEvent.IndicatorType.DAMAGE);
    }

    /**
     * If MythicLib can find a damager, display the hologram
     * in a cone which direction is the damager-target line.
     *
     * @param event Damage event
     * @return Direction of the hologram
     */
    private Vector getDirection(EntityDamageEvent event) {

        if (event instanceof EntityDamageByEntityEvent) {
            Vector dir = event.getEntity().getLocation().toVector().subtract(((EntityDamageByEntityEvent) event).getDamager().getLocation().toVector()).setY(0);
            if (dir.lengthSquared() > 0) {

                // Calculate angle of attack
                double x = dir.getX(), z = dir.getZ();
                double a = z == 0 ? (x < 0 ? Math.PI / 2 : -Math.PI / 2) : (z < 0 ? Math.PI : 0) - Math.atan(x / z);

                // Random angle offset
                a += Math.PI / 2 * (random.nextDouble() - .5);

                return new Vector(Math.cos(a), 0, Math.sin(a));
            }
        }

        double a = random.nextDouble() * Math.PI * 2;
        return new Vector(Math.cos(a), 0, Math.sin(a));
    }

    /**
     * @return Normalized vector, or null vector if it's null
     */
    private Vector normalize(Vector vec) {
        return vec.lengthSquared() == 0 ? vec : vec.normalize();
    }
}
