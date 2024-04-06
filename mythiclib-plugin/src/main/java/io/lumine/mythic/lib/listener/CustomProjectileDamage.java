package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.entity.ProjectileMetadata;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CustomProjectileDamage implements Listener {

    /**
     * This event is called on LOWEST and only edits the custom bow base damage.
     * It does NOT take into account the base damage passed in Bow#getDamage()
     * and fully overrides the Bukkit value.
     * <p>
     * This applies to tridents, arrows, spectral arrows etc.
     * <p>
     * Event order: ProjectileHit -> EntityDamage / EntityDeathEvent
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void customProjectileDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile) || !(event.getEntity() instanceof LivingEntity) || event.getEntity().hasMetadata("NPC"))
            return;

        final Projectile projectile = (Projectile) event.getDamager();
        final ProjectileMetadata data = ProjectileMetadata.get(projectile);
        if (data == null) return;

        /*
         * CUSTOM PROJECTILE DAMAGE FORMULA
         */
        if (data.isCustomDamage()) {
            double baseDamage = data.getDamage();

            // Apply power vanilla enchant
            if (projectile instanceof AbstractArrow
                    && data.getSourceItem() != null
                    && data.getSourceItem().getItem().hasItemMeta()
                    && data.getSourceItem().getItem().getItemMeta().getEnchants().containsKey(Enchantment.ARROW_DAMAGE))
                baseDamage *= 1.25 + (.25 * data.getSourceItem().getItem().getItemMeta().getEnchantLevel(Enchantment.ARROW_DAMAGE));

            event.setDamage(baseDamage);
        }
    }
}
