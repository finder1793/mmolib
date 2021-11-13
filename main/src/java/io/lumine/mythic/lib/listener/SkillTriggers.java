package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.event.PlayerKillEntityEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.util.ProjectileTicker;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SkillTriggers implements Listener {

    @EventHandler
    public void killEntity(PlayerKillEntityEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        caster.triggerSkills(TriggerType.KILL_ENTITY, event.getTarget());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void attack(PlayerAttackEvent event) {
        event.getData().triggerSkills(TriggerType.ATTACK, event.getAttack(), event.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void damagedByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && MMOPlayerData.has(event.getEntity().getUniqueId())
                && MythicLib.plugin.getEntities().canTarget((Player) event.getEntity(), event.getDamager(), InteractionType.OFFENSE_SKILL)) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.DAMAGED_BY_ENTITY, event.getDamager());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void damaged(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && MMOPlayerData.has(event.getEntity().getUniqueId())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.DAMAGED, null);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void death(PlayerDeathEvent event) {
        if (MMOPlayerData.has(event.getEntity())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.DEATH, null);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void login(PlayerJoinEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        caster.triggerSkills(TriggerType.LOGIN, null);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void shootBow(EntityShootBowEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && MMOPlayerData.has(event.getEntity().getUniqueId())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.SHOOT_BOW, event.getProjectile());

            // Register a runnable to trigger ARROW_TICK
            new ProjectileTicker(caster, TriggerType.ARROW_TICK, event.getProjectile());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void shootTrident(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Trident && event.getEntity() instanceof Player && MMOPlayerData.has(event.getEntity().getUniqueId())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.SHOOT_TRIDENT, event.getEntity());

            // Register a runnable to trigger TRIDENT_TICK
            new ProjectileTicker(caster, TriggerType.TRIDENT_TICK, event.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void sneak(PlayerToggleSneakEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        caster.triggerSkills(TriggerType.SNEAK, null);
    }

    /**
     * {@link Cancellable#isCancelled()} does not work with PlayerInteractEvent
     * because there are now two possible ways to cancel the event, either
     * by canceling the item interaction, either by canceling the block interaction.
     * <p>
     * Checking if the event is cancelled points towards the block interaction
     * and not the item interaction which  is NOT what MythicLibis interested in
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void click(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL || event.useItemInHand() == Event.Result.DENY)
            return;

        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        boolean left = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK, sneaking = event.getPlayer().isSneaking();
        TriggerType type = sneaking ? (left ? TriggerType.SHIFT_LEFT_CLICK : TriggerType.SHIFT_RIGHT_CLICK) : (left ? TriggerType.LEFT_CLICK : TriggerType.RIGHT_CLICK);
        caster.triggerSkills(type, null);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void projectileHits(ProjectileHitEvent event) {

        // Make sure it's an arrow or a trident
        if (event.getEntityType() != EntityType.ARROW && event.getEntityType() != EntityType.TRIDENT)
            return;

        // Make sure the shooter is a valid player
        if (!(event.getEntity().getShooter() instanceof Player) || !MMOPlayerData.has(((Player) event.getEntity().getShooter()).getUniqueId()))
            return;

        // Find the right target, either the target if it hit an entity, or the arrow if it landed on a block
        boolean hit = event.getHitEntity() != null;
        Entity target = hit ? event.getHitEntity() : event.getEntity();

        // Find the trigger type
        TriggerType triggerType = event.getEntityType() == EntityType.ARROW ? (hit ? TriggerType.ARROW_HIT : TriggerType.ARROW_LAND) : (hit ? TriggerType.TRIDENT_HIT : TriggerType.TRIDENT_LAND);

        MMOPlayerData caster = MMOPlayerData.get(((Player) event.getEntity().getShooter()).getUniqueId());
        caster.triggerSkills(triggerType, target);
    }
}
