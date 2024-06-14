package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.SharedStat;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.api.stat.handler.AttributeStatHandler;
import io.lumine.mythic.lib.api.stat.handler.DelegateStatHandler;
import io.lumine.mythic.lib.api.stat.handler.MovementSpeedStatHandler;
import io.lumine.mythic.lib.api.stat.handler.StatHandler;
import io.lumine.mythic.lib.util.ConfigFile;
import org.apache.commons.lang.Validate;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;

public class StatManager {
    private final Map<String, StatHandler> handlers = new HashMap<>();

    public void initialize(boolean clearBefore) {
        if (clearBefore) handlers.clear();
        else UtilityMethods.loadDefaultFile("", "stats.yml");

        final ConfigurationSection statsConfig = new ConfigFile("stats").getConfig();

        // Default stat handlers
        try {
            handlers.put(SharedStat.ARMOR, new AttributeStatHandler(statsConfig, Attribute.GENERIC_ARMOR, SharedStat.ARMOR));
            handlers.put(SharedStat.ARMOR_TOUGHNESS, new AttributeStatHandler(statsConfig, Attribute.GENERIC_ARMOR_TOUGHNESS, SharedStat.ARMOR_TOUGHNESS));
            handlers.put(SharedStat.ATTACK_DAMAGE, new AttributeStatHandler(statsConfig, Attribute.GENERIC_ATTACK_DAMAGE, SharedStat.ATTACK_DAMAGE));
            handlers.put(SharedStat.ATTACK_SPEED, new AttributeStatHandler(statsConfig, Attribute.GENERIC_ATTACK_SPEED, SharedStat.ATTACK_SPEED));
            handlers.put(SharedStat.KNOCKBACK_RESISTANCE, new AttributeStatHandler(statsConfig, Attribute.GENERIC_KNOCKBACK_RESISTANCE, SharedStat.KNOCKBACK_RESISTANCE));
            handlers.put(SharedStat.MAX_HEALTH, new AttributeStatHandler(statsConfig, Attribute.GENERIC_MAX_HEALTH, SharedStat.MAX_HEALTH));
            final StatHandler msStatHandler = new MovementSpeedStatHandler(statsConfig);
            handlers.put(SharedStat.MOVEMENT_SPEED, msStatHandler);
            handlers.put(SharedStat.SPEED_MALUS_REDUCTION, new DelegateStatHandler(statsConfig, SharedStat.SPEED_MALUS_REDUCTION, msStatHandler));

            // 1.20.2
            if (MythicLib.plugin.getVersion().isAbove(1, 20, 2))
                handlers.put(SharedStat.MAX_ABSORPTION, new AttributeStatHandler(statsConfig, Attribute.GENERIC_MAX_ABSORPTION, SharedStat.MAX_ABSORPTION));

            // 1.20.5
            if (MythicLib.plugin.getVersion().isAbove(1, 20, 5)) {
                handlers.put(SharedStat.BLOCK_BREAK_SPEED, new AttributeStatHandler(statsConfig, Attribute.PLAYER_BLOCK_BREAK_SPEED, SharedStat.BLOCK_BREAK_SPEED));
                handlers.put(SharedStat.BLOCK_INTERACTION_RANGE, new AttributeStatHandler(statsConfig, Attribute.PLAYER_BLOCK_INTERACTION_RANGE, SharedStat.BLOCK_INTERACTION_RANGE));
                handlers.put(SharedStat.ENTITY_INTERACTION_RANGE, new AttributeStatHandler(statsConfig, Attribute.PLAYER_ENTITY_INTERACTION_RANGE, SharedStat.ENTITY_INTERACTION_RANGE));
                handlers.put(SharedStat.FALL_DAMAGE_MULTIPLIER, new AttributeStatHandler(statsConfig, Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER, SharedStat.FALL_DAMAGE_MULTIPLIER));
                handlers.put(SharedStat.GRAVITY, new AttributeStatHandler(statsConfig, Attribute.GENERIC_GRAVITY, SharedStat.GRAVITY));
                handlers.put(SharedStat.JUMP_STRENGTH, new AttributeStatHandler(statsConfig, Attribute.GENERIC_JUMP_STRENGTH, SharedStat.JUMP_STRENGTH));
                handlers.put(SharedStat.SAFE_FALL_DISTANCE, new AttributeStatHandler(statsConfig, Attribute.GENERIC_SAFE_FALL_DISTANCE, SharedStat.SAFE_FALL_DISTANCE));
                handlers.put(SharedStat.SCALE, new AttributeStatHandler(statsConfig, Attribute.GENERIC_SCALE, SharedStat.SCALE));
                handlers.put(SharedStat.STEP_HEIGHT, new AttributeStatHandler(statsConfig, Attribute.GENERIC_STEP_HEIGHT, SharedStat.STEP_HEIGHT));
            }

        } catch (Exception exception) {
            MythicLib.plugin.getLogger().log(Level.WARNING, "Could not load default stat handlers:");
            exception.printStackTrace();
        }

        // Load stat handlers
        for (String key : collectReferencedStats(statsConfig))
            try {
                final String stat = UtilityMethods.enumName(key);
                handlers.putIfAbsent(stat, new StatHandler(statsConfig, stat));
            } catch (RuntimeException exception) {
                MythicLib.plugin.getLogger().log(Level.WARNING, "Could not load stat handler '" + key + "': " + exception.getMessage());
            }
    }

    @NotNull
    private Iterable<String> collectReferencedStats(ConfigurationSection config) {
        final List<String> keys = new ArrayList<>();
        for (String key : config.getKeys(false))
            keys.addAll(config.getConfigurationSection(key).getKeys(false));
        return keys;
    }

    @NotNull
    public static String format(String stat, MMOPlayerData player) {
        return player.getStatMap().getInstance(stat).formatFinal();
    }

    @NotNull
    public static String format(String stat, double value) {
        @Nullable final StatHandler handler = MythicLib.plugin.getStats().handlers.get(stat);
        return (handler == null ? MythicLib.plugin.getMMOConfig().decimal : handler.getDecimalFormat()).format(value);
    }

    /**
     * Lets the MMO- plugins knows that a specific stat needs an update
     * whenever the value of the player stat changes (due to a MythicLib
     * stat modifier being added/being removed/expiring).
     *
     * @param handler Behaviour of given stat
     */
    public void registerStat(@NotNull StatHandler handler, String... aliases) {
        Validate.notNull(handler, "StatHandler cannot be null");

        handlers.put(handler.getStat(), handler);
        for (String alias : aliases)
            handlers.put(alias, handler);
    }

    @NotNull
    public Optional<StatHandler> getHandler(String stat) {
        return Optional.ofNullable(handlers.get(stat));
    }

    public boolean isRegistered(String stat) {
        return handlers.containsKey(stat);
    }

    @NotNull
    public Set<String> getRegisteredStats() {
        return handlers.keySet();
    }

    @NotNull
    public Collection<StatHandler> getHandlers() {
        return handlers.values();
    }

    public void clearRegisteredStats(Predicate<StatHandler> filter) {
        final Iterator<StatHandler> ite = handlers.values().iterator();
        while (ite.hasNext()) if (filter.test(ite.next())) ite.remove();
    }

    @Deprecated
    public void runUpdate(StatMap map, String stat) {
        map.getInstance(stat).update();
    }

    @Deprecated
    public void runUpdates(@NotNull StatMap map) {
        for (StatInstance ins : map.getInstances()) ins.update();
    }

    @Deprecated
    public void runUpdate(@NotNull StatInstance instance) {
        instance.update();
    }

    @Deprecated
    public double getBaseValue(String stat, StatMap map) {
        @Nullable final StatHandler handler = handlers.get(stat);
        return handler == null ? 0 : handler.getBaseValue(map.getInstance(stat));
    }

    @Deprecated
    public double getBaseValue(StatInstance instance) {
        return instance.getBase();
    }

    @Deprecated
    public double getTotalValue(String stat, StatMap map) {
        return map.getStat(stat);
    }

    @Deprecated
    public double getTotalValue(StatInstance instance) {
        return instance.getTotal();
    }

    @Deprecated
    public void registerStat(String stat, StatHandler handler) {
        Validate.notNull(stat, "Stat cannot be null");
        Validate.notNull(handler, "StatHandler cannot be null");

        handlers.put(stat, handler);
    }
}
