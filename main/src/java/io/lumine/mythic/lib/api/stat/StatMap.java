package io.lumine.mythic.lib.api.stat;

import io.lumine.mythic.lib.api.AttackResult;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatMap {

    private final MMOPlayerData data;
    private final Map<String, StatInstance> stats = new ConcurrentHashMap<>();

    public StatMap(MMOPlayerData player) {
        this.data = player;
    }

    /**
     * @return The StatMap owner ie the corresponding MMOPlayerData
     */
    public MMOPlayerData getPlayerData() {
        return data;
    }

    /**
     * @param id
     *            The string key of the stat
     * @return The value of the stat after applying stat modifiers
     */
    public double getStat(String id) {
        return getInstance(id).getTotal();
    }

    /**
     * @param id
     *            The string key of the stat
     * @return The corresponding StatInstance, which can be manipulated to add
     *         (temporary?) stat modifiers to a player, remove modifiers or
     *         calculate stat values in various ways. StatInstances are
     *         completely flushed when the server restarts
     */
    public StatInstance getInstance(String id) {
        if (stats.containsKey(id))
            return stats.get(id);

        StatInstance ins = new StatInstance(this, id);
        stats.put(id, ins);
        return ins;
    }

    /**
     * @return The StatInstances that have been manipulated so far since the
     *         player has logged in. StatInstances are completely flushed when
     *         the server restarts
     */
    public Collection<StatInstance> getInstances() {
        return stats.values();
    }

    /**
     * Some stats like movement speed, attack damage.. are based on vanilla
     * player attributes. Every time a stat modifier is added to a StatInstance
     * in MMOLib, MMOLib needs to perform a further attribute modifier update.
     * This method runs all the updates for the vanilla-attribute-based MMOLib
     * stats.
     */
    @Deprecated
    public void updateAll() {
        MMOLib.plugin.getStats().runUpdates(this);
    }

    /***
     * Runs a specific stat update for a specific StatMap
     *
     * @param stat
     *            The string key of the stat which needs update
     */
    @Deprecated
    public void update(String stat) {
        MMOLib.plugin.getStats().runUpdate(this, stat);
    }

    /**
     * @return Some actions require the player stats to be temporarily saved.
     *         When a player casts a projectile skill, there's a brief delay
     *         before it hits the target: the stat values taken into account
     *         correspond to the stat values when the player cast the skill (not
     *         when it finally hits the target). This cache technique fixes a
     *         huge game breaking glitch
     */
    public CachedStatMap cache() {
        return new CachedStatMap();
    }

    public class CachedStatMap {
        private final Player player;
        private final Map<String, Double> cached = new HashMap<>();

        private CachedStatMap() {
            this.player = data.getPlayer();
            for (String key : stats.keySet())
                cached.put(key, getStat(key));
        }

        /**
         * @return The cached Player instance. Player instances are cached so
         *         that even if the player logs out, the ability can still be
         *         cast without additional errors
         */
        public Player getPlayer() {
            return player;
        }

        public MMOPlayerData getData() {
            return data;
        }

        /**
         * @param stat
         *            The string key of the stat
         * @return The cached stat value, or the vanilla
         */
        public double getStat(String stat) {
            return cached.getOrDefault(stat, getInstance(stat).getBase());
        }

        /**
         * Edits the current cached stat value
         *
         * @param stat
         *            The string key of the stat
         * @param value
         *            The value you want to cache
         */
        public void setStat(String stat, double value) {
            cached.put(stat, value);
        }

        public void damage(LivingEntity target, double value, DamageType... types) {
            MMOLib.plugin.getDamage().damage(player, target, new AttackResult(value, types));
        }
    }
}
