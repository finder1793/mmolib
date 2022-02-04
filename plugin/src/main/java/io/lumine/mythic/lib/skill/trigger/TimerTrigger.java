package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TimerTrigger extends TriggerType {

    public static void deploy() {

        (new BukkitRunnable() {
            public void run() {

                // Tick all triggers
                for (Map.Entry<Integer, TimerTrigger> entries : timerTriggers.entrySet()) {

                    // All right, decrease tick
                    if (entries.getValue().tick()) {

                        // Ticks all players
                        for (Player p : Bukkit.getOnlinePlayers()) {

                            // Tick this player
                            MMOPlayerData caster = MMOPlayerData.get(p);
                            caster.triggerSkills(entries.getValue(), p);
                        } }
                    }
                }


        }).runTaskTimer(MythicLib.plugin, 20L, 1L);
    }

    /**
     * When set to anything greater than zero, will trigger every
     * these many server ticks.
     *
     * @return Number of ticks of period
     */
    @Range(from = 1, to = Integer.MAX_VALUE) public int getBaseTicks() { return baseTicks; }
    @Range(from = 1, to = Integer.MAX_VALUE) private final int baseTicks;

    /**
     * Will count down from base ticks to zero every tick,
     * triggering this trigger when it reaches zero.
     *
     * @return Current countdown
     */
    public int getTicks() { return ticks; }
    int ticks;

    public boolean tick() {

        // Tick once
        ticks--;

        // Ticks reached zero? its time.
        if (ticks == 0) {
            ticks = getBaseTicks();
            return true; }

        // Not zero, not ticking time
        return false;
    }

    TimerTrigger(@NotNull String internal_name, @Range(from = 1, to = Integer.MAX_VALUE) int baseTicks) {
        super(internal_name);
        this.baseTicks = baseTicks;
        ticks = baseTicks;
    }

    TimerTrigger(@NotNull String internal_name, boolean silent, @Range(from = 1, to = Integer.MAX_VALUE) int baseTicks) {
        super(internal_name, silent);
        this.baseTicks = baseTicks;
        ticks = baseTicks;
    }

    @Override public boolean is(@NotNull String format) { return name().equals(format); }
    @NotNull @Override public String name() { return super.name() + "_" + baseTicks; }

    @NotNull public static TimerTrigger newTimerTrigger(@Range(from = 1, to = Integer.MAX_VALUE) int ticks) {

        // One existing?
        TimerTrigger ret = timerTriggers.get(ticks);
        if (ret != null) { return ret; }

        // All right then
        ret = new TimerTrigger("TIMER", ticks);
        timerTriggers.put(ret.getBaseTicks(), ret);
        return ret;
    }

    @NotNull static final HashMap<Integer, TimerTrigger> timerTriggers = new HashMap<>();
}
