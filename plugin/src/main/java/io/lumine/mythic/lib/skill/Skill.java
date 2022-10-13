package io.lumine.mythic.lib.skill;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.skill.PlayerCastSkillEvent;
import io.lumine.mythic.lib.api.event.skill.SkillCastEvent;
import io.lumine.mythic.lib.api.util.TemporaryListener;
import io.lumine.mythic.lib.player.cooldown.CooldownObject;
import io.lumine.mythic.lib.player.modifier.PlayerModifier;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.SkillResult;
import io.lumine.mythic.lib.skill.trigger.TriggerMetadata;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Implemented by MMOItems abilities or MMOCore class skills.
 * <p>
 * This class implements all skill restrictions and behaviours
 * that are SPECIFIC to MMOItems or MMOCore like resource costs,
 * cooldown messages, no-cooldown modes...
 *
 * @author jules
 */
public abstract class Skill implements CooldownObject {
    private final TriggerType trigger;

    public Skill(TriggerType trigger) {
        this.trigger = Objects.requireNonNull(trigger, "Trigger cannot be null");
    }

    public SkillResult cast(TriggerMetadata triggerMeta) {
        return cast(triggerMeta, 0);
    }

    public SkillResult cast(TriggerMetadata triggerMeta, int delay) {
        return cast(triggerMeta.toSkillMetadata(this), delay);
    }

    public <T extends SkillResult> SkillResult cast(SkillMetadata meta) {
        return cast(meta, 0);
    }

    /**
     * Used when casting a skill with a delay to it. The player must not move in this delay otherwise the skill will be canceled.
     */
    public <T extends SkillResult> SkillResult cast(SkillMetadata meta, int delay) {
        SkillHandler<T> handler = (SkillHandler<T>) getHandler();

        // Lower level skill restrictions
        T result = handler.getResult(meta);
        if (!result.isSuccessful(meta))
            return result;

        // High level skill restrictions
        if (!getResult(meta))
            return result;

        // Call first Bukkit event
        PlayerCastSkillEvent called1 = new PlayerCastSkillEvent(meta, result);
        Bukkit.getPluginManager().callEvent(called1);
        if (called1.isCancelled())
            return result;

        //If the delay is null we cast normally the skill
        if (delay == 0) {
            onCast(meta, result);
            return result;
        }

        NamespacedKey bossbarNamespacedKey = new NamespacedKey(MythicLib.plugin, "mmocore_quest_progress_" + meta.getCaster().getPlayer().getUniqueId().toString());
        BossBar bossbar = Bukkit.createBossBar(bossbarNamespacedKey, "CASTING", BarColor.WHITE, BarStyle.SEGMENTED_20);
        bossbar.addPlayer(meta.getCaster().getPlayer());
        //Implement a runnable to run the task later
        BukkitRunnable runnable = new BukkitRunnable() {
            private int counter = delay;

            @Override
            public void run() {

                bossbar.setProgress((double) (delay - counter) / (double) delay);
                counter--;
                if (counter <= 0) {
                    onCast(meta, result);
                    bossbar.removeAll();
                    cancel();
                }
            }
        };
        runnable.runTaskTimer(MythicLib.plugin, 0L, 1L);

        //Listener that cancels the event if the player moves.
        TemporaryListener temporaryListener = new TemporaryListener(PlayerMoveEvent.getHandlerList()) {
            @EventHandler
            public void onMove(PlayerMoveEvent event) {
                if (event.getPlayer().equals(meta.getCaster().getPlayer()))
                    event.setCancelled(true);
            }

            @Override
            public void whenClosed() {

            }
        };

        //Closes the listener after the delay to avoid memory issues.
        new BukkitRunnable() {
            @Override
            public void run() {
                temporaryListener.close();
            }
        }.runTaskLater(MythicLib.plugin, delay);


        return result;
    }

    /**
     * This method should be used to check for resource costs
     * or other skill limitations.
     * <p>
     * Runs last after {@link SkillHandler#getResult(SkillMetadata)}
     *
     * @param skillMeta Info of skill being cast
     * @return If the skill can be cast
     */
    @NotNull
    public abstract boolean getResult(SkillMetadata skillMeta);


    /**
     * Everything that happens when a skill is cast.
     */
    public <T extends SkillResult> void onCast(SkillMetadata meta, T result) {
        SkillHandler<T> handler = (SkillHandler<T>) getHandler();
        // High level skill effects
        whenCast(meta);

        // Lower level skill effects
        handler.whenCast(result, meta);

        // Call second Bukkit event
        Bukkit.getPluginManager().callEvent(new SkillCastEvent(meta, result));
    }

    /**
     * This is NOT where the actual skill effects are applied.
     * <p>
     * This method should be used to handle resource costs
     * or cooldown messages if required.
     * <p>
     * Runs first before {@link SkillHandler#whenCast(SkillResult, SkillMetadata)}
     *
     * @param skillMeta Info of skill being cast
     */
    public abstract void whenCast(SkillMetadata skillMeta);

    public abstract SkillHandler<?> getHandler();

    /**
     * This contains the following information:
     * - whether or not the skill is active or passive
     * - whether or not the skill is silent
     *
     * @return Context in which this skill is triggered
     */
    @NotNull
    public TriggerType getTrigger() {
        return trigger;
    }

    public abstract double getModifier(String path);

    @Override
    public String getCooldownPath() {
        return "skill_" + getHandler().getId();
    }
}
