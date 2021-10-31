package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.event.PlayerKillEntityEvent;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.player.EquipmentSlot;
import io.lumine.mythic.lib.player.MMOPlayerData;
import io.lumine.mythic.lib.skill.metadata.TriggerMetadata;
import io.lumine.mythic.lib.skill.trigger.PassiveSkill;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SkillTriggers implements Listener {

    @EventHandler
    public void killEntity(PlayerKillEntityEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        triggerSkills(TriggerType.KILL_ENTITY, caster, event.getTarget());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void attack(PlayerAttackEvent event) {
        triggerSkills(TriggerType.ATTACK, event.getAttack(), event.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void damagedByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && MMOPlayerData.has(event.getEntity().getUniqueId())
                && MythicLib.plugin.getEntities().canTarget((Player) event.getEntity(), event.getDamager(), InteractionType.OFFENSE_SKILL)) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            triggerSkills(TriggerType.DAMAGED_BY_ENTITY, caster, event.getDamager());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void damaged(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && MMOPlayerData.has(event.getEntity().getUniqueId())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            triggerSkills(TriggerType.DAMAGED, caster, null);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void death(PlayerDeathEvent event) {
        if (MMOPlayerData.has(event.getEntity())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            triggerSkills(TriggerType.DEATH, caster, null);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void login(PlayerJoinEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        triggerSkills(TriggerType.LOGIN, caster, null);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void shootBow(EntityShootBowEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && MMOPlayerData.has(event.getEntity().getUniqueId())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            triggerSkills(TriggerType.SHOOT_BOW, caster, event.getProjectile());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void shootTrident(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Trident && event.getEntity() instanceof Player && MMOPlayerData.has(event.getEntity().getUniqueId())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            triggerSkills(TriggerType.SHOOT_TRIDENT, caster, event.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void sneak(PlayerToggleSneakEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        triggerSkills(TriggerType.SNEAK, caster, null);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void click(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL)
            return;

        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        boolean left = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK, sneaking = event.getPlayer().isSneaking();
        TriggerType type = sneaking ? (left ? TriggerType.SHIFT_LEFT_CLICK : TriggerType.SHIFT_RIGHT_CLICK) : (left ? TriggerType.LEFT_CLICK : TriggerType.RIGHT_CLICK);
        triggerSkills(type, caster, null);
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
        triggerSkills(triggerType, caster, target);
    }

    private void triggerSkills(TriggerType triggerType, MMOPlayerData caster, Entity target) {
        triggerSkills(triggerType, new AttackMetadata(new DamageMetadata(), caster.getStatMap().cache(EquipmentSlot.MAIN_HAND)), target);
    }

    private void triggerSkills(TriggerType triggerType, AttackMetadata attackMetadata, Entity target) {
        TriggerMetadata casterMeta = new TriggerMetadata(attackMetadata, target);

        // TODO check for the WG flag

        for (PassiveSkill trigger : attackMetadata.getStats().getData().getPassiveSkills())
            if (trigger.getType() == triggerType)
                trigger.getTriggeredSkill().execute(casterMeta);
    }
}
