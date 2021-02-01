package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.api.stat.SharedStat;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.api.stat.handler.AttributeStatHandler;
import io.lumine.mythic.lib.api.stat.handler.MovementSpeedStatHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.attribute.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class StatManager {

    private final Map<String, Consumer<StatMap>> updates = new HashMap<>();
    private final Map<String, Double> baseValues = new HashMap<>();

    public StatManager() {

        /*
         * default stat updates
         */
        updates.put(SharedStat.ARMOR, new AttributeStatHandler(Attribute.GENERIC_ARMOR, SharedStat.ARMOR));
        updates.put(SharedStat.ARMOR_TOUGHNESS, new AttributeStatHandler(Attribute.GENERIC_ARMOR_TOUGHNESS, SharedStat.ARMOR_TOUGHNESS));

        updates.put(SharedStat.ATTACK_DAMAGE, new AttributeStatHandler(Attribute.GENERIC_ATTACK_DAMAGE, SharedStat.ATTACK_DAMAGE));
        updates.put(SharedStat.ATTACK_SPEED, new AttributeStatHandler(Attribute.GENERIC_ATTACK_SPEED, SharedStat.ATTACK_SPEED));
        updates.put(SharedStat.KNOCKBACK_RESISTANCE,
                new AttributeStatHandler(Attribute.GENERIC_KNOCKBACK_RESISTANCE, SharedStat.KNOCKBACK_RESISTANCE));
        updates.put(SharedStat.MAX_HEALTH, new AttributeStatHandler(Attribute.GENERIC_MAX_HEALTH, SharedStat.MAX_HEALTH));

        Consumer<StatMap> moveSpeed = new MovementSpeedStatHandler();
        updates.put(SharedStat.MOVEMENT_SPEED, moveSpeed);
        updates.put(SharedStat.SPEED_MALUS_REDUCTION, moveSpeed);

        /*
         * default stat base values
         */
        baseValues.put("MAX_HEALTH", 20d);
        baseValues.put("MOVEMENT_SPEED", .1);
        baseValues.put("ATTACK_DAMAGE", 1d);
        baseValues.put("ATTACK_SPEED", 4d);
    }

    /***
     * Some stats like movement speed, attack damage.. are based on vanilla
     * player attributes. Every time a stat modifier is added to a StatInstance
     * in MMOLib, MMOLib needs to perform a further attribute modifier update.
     * This method runs all the updates for the vanilla-attribute-based MMOLib
     * stats.
     *
     * @param map
     *            The StatMap of the player who needs update
     */
    public void runUpdates(StatMap map) {
        updates.values().forEach(update -> update.accept(map));
    }

    /***
     * Runs a specific stat update for a specific StatMap
     *
     * @param map
     *            The StatMap of the player who needs update
     * @param stat
     *            The string key of the stat which needs update
     */
    public void runUpdate(StatMap map, String stat) {
        if (updates.containsKey(stat))
            updates.get(stat).accept(map);
    }

    /***
     * @param stat
     *            The string key of the stat
     * @return The base value of this stat, or 0 if it does not have any
     */
    public double getBaseValue(String stat) {
        return baseValues.getOrDefault(stat, 0d);
    }

    /***
     * Lets MMOCore knows that a specific stat needs an update whenever the
     * value of the player stat changes (due to a MMOLib stat modifier being
     * added/being removed/expiring).
     *
     * @param stat
     *            The string key of the stat
     * @param update
     *            The consumer being called whenever this stat needs update
     */
    public void registerUpdate(String stat, Consumer<StatMap> update) {
        Validate.notNull(stat, "Stat cannot be null");
        Validate.notNull(update, "StatMap update cannot be null");

        updates.put(stat, update);
    }

    /***
     * Lets MMOCore knows that a specific stat has a base stat value of X. This
     * is sometimes needed for stats which are based on vanilla player
     * attributes. Attack damage has a base of 1, movement speed .1, max health
     * 20, etc. This method MUST be called when MMOCore is loaded, before any
     * MMOPlayerData is loaded, because these base values are cached whenever
     * StatInstances are instanced.
     *
     * @param stat
     *            The string key of the stat
     * @param base
     *            The base value you want to register
     */
    public void registerBaseValue(String stat, double base) {
        Validate.notNull(stat, "Stat cannot be null");
        Validate.notNull(base != 0, "Base value cannot be 0 otherwise useless");

        baseValues.put(stat, base);
    }
}
