package io.lumine.mythic.lib.api.util;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class is used to trigger the right _TICK
 * skill trigger every tick when the projectile is
 * still flying in the air.
 * <p>
 * This is used for both tridents and arrows
 *
 * @author indyuce
 */
public class ProjectileTicker extends TemporaryListener {
    private final int entityId;
    private final BukkitRunnable runnable;

    public ProjectileTicker(MMOPlayerData caster, TriggerType triggerType, Entity proj) {
        super(ProjectileHitEvent.getHandlerList(), EntityDeathEvent.getHandlerList());

        this.entityId = proj.getEntityId();
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                caster.triggerSkills(triggerType, proj);
            }
        };
        runnable.runTaskTimer(MythicLib.plugin, 0, 1);
    }

    @EventHandler
    public void unregisterOnHit(ProjectileHitEvent event) {
        if (event.getEntity().getEntityId() == entityId)
            close();
    }

    @EventHandler
    public void unregisterOnDeath(EntityDeathEvent event) {
        if (event.getEntity().getEntityId() == entityId)
            close();
    }

    @Override
    public void whenClosed() {
        runnable.cancel();
    }
}
