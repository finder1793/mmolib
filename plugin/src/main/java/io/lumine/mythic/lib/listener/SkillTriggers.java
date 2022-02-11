package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.event.PlayerKillEntityEvent;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import io.lumine.mythic.lib.util.ProjectileTrigger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SkillTriggers implements Listener {

    public SkillTriggers() {
        Bukkit.getScheduler().runTaskTimer(MythicLib.plugin, () -> MMOPlayerData.forEachOnline(online -> online.getPassiveSkillMap().tickTimerSkills()), 0, 1);
    }

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
        if (event.getEntity() instanceof Player && MMOPlayerData.has(event.getEntity().getUniqueId())
                && MythicLib.plugin.getEntities().canTarget((Player) event.getEntity(), event.getDamager(), InteractionType.OFFENSE_SKILL)) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.DAMAGED_BY_ENTITY, event.getDamager());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void damaged(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && MMOPlayerData.has(event.getEntity().getUniqueId())) {
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
        if (event.getEntity() instanceof Player && MMOPlayerData.has(event.getEntity().getUniqueId())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());

            if (!syncInfiniteRecursionBlock) {

                // Trigger skills only if not within a stacked call
                syncInfiniteRecursionBlock = true;
                caster.triggerSkills(TriggerType.SHOOT_BOW, event.getProjectile());
                syncInfiniteRecursionBlock = false;
            }

            // Register a runnable to trigger projectile skills
            EquipmentSlot hand = getShootHand(((Player) event.getEntity()).getInventory());
            new ProjectileTrigger(caster, ProjectileTrigger.ProjectileType.ARROW, event.getProjectile(), hand);
        }
    }

    /*
     * Its pretty funny when a bow skill can trigger another bow
     * and it causes the bow to fire again at 0 cooldown but uuuh
     * yeah this should do the trick to prevent this from feeding
     * itself forever.
     */
    static boolean syncInfiniteRecursionBlock = false;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void shootTrident(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Trident && event.getEntity().getShooter() instanceof Player && MMOPlayerData.has((Player) event.getEntity().getShooter())) {
            Player shooter = (Player) event.getEntity().getShooter();
            MMOPlayerData caster = MMOPlayerData.get(shooter);
            caster.triggerSkills(TriggerType.SHOOT_TRIDENT, event.getEntity());

            // Register a runnable to trigger projectile skills
            EquipmentSlot hand = getShootHand(shooter.getInventory());
            new ProjectileTrigger(caster, ProjectileTrigger.ProjectileType.TRIDENT, event.getEntity(), hand);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void sneak(PlayerToggleSneakEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        caster.triggerSkills(TriggerType.SNEAK, null);
    }

    /**
     * @implNote {@link Cancellable#isCancelled()} does not work with PlayerInteractEvent
     *         because there are now two possible ways to cancel the event, either
     *         by canceling the item interaction, either by canceling the block interaction.
     *         <p>
     *         Checking if the event is cancelled points towards the block interaction
     *         and not the item interaction which is NOT what MythicLib is interested in
     * @implNote Scrap this, it's 100% useless to check if the event is cancelled.
     *         It makes sense to trigger skills even if the item or block interactions are canceled
     * @implNote Event priority set to {@link EventPriority#LOW} because MI consumes consumables on
     *         priority NORMAL and item abilities require the held item not to be null in hand
     */
    @EventHandler(priority = EventPriority.LOW)
    public void click(PlayerInteractEvent event) {
        // || event.useItemInHand() == Event.Result.DENY
        if (event.getAction() == Action.PHYSICAL)
            return;

        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        boolean left = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK, sneaking = event.getPlayer().isSneaking();
        TriggerType type = sneaking ? (left ? TriggerType.SHIFT_LEFT_CLICK : TriggerType.SHIFT_RIGHT_CLICK) : (left ? TriggerType.LEFT_CLICK : TriggerType.RIGHT_CLICK);
        caster.triggerSkills(type, null);
    }

    /**
     * @return Hand used to shoot a projectile (arrow/trident) based on
     * what items the player is holding in his two hands
     */
    private EquipmentSlot getShootHand(PlayerInventory inv) {
        ItemStack main = inv.getItemInMainHand();
        return main != null && isShootable(main.getType()) ? EquipmentSlot.MAIN_HAND : EquipmentSlot.OFF_HAND;
    }

    private boolean isShootable(Material mat) {
        return mat == Material.BOW || mat == Material.CROSSBOW || mat == Material.TRIDENT;
    }
}
