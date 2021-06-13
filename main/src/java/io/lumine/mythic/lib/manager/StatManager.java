package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.api.stat.SharedStat;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.api.stat.handler.AttributeStatHandler;
import io.lumine.mythic.lib.api.stat.handler.MovementSpeedStatHandler;
import io.lumine.mythic.lib.api.stat.handler.StatHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.attribute.Attribute;

import java.util.HashMap;
import java.util.Map;

/**
 * Can be used by external plugins to register complex behaviours for numeric
 * player statistics. This class helps define a base value for stats as well
 * as specific actions performed when the player stat value changes (stat updates).
 * <p>
 * This is particularly important e.g for stats based on vanilla attributes
 * like max health or movement speed, which player attributes need to be updated
 * as soon as the stat value is updated.
 *
 * @author indyuce
 */
public class StatManager {

    /**
     * Stat handlers for every registered stats. We are using strings for keys so that any
     * plugin can register its own stat. However, default stats shared by MMOItems and MythicCore
     * can be found in the SharedStat public enum.
     */
    private final Map<String, StatHandler> handlers = new HashMap<>();

    public StatManager() {

        /*
         * default stat handlers
         */
        handlers.put(SharedStat.ARMOR, new AttributeStatHandler(Attribute.GENERIC_ARMOR, SharedStat.ARMOR));
        handlers.put(SharedStat.ARMOR_TOUGHNESS, new AttributeStatHandler(Attribute.GENERIC_ARMOR_TOUGHNESS, SharedStat.ARMOR_TOUGHNESS));

        handlers.put(SharedStat.ATTACK_DAMAGE, new AttributeStatHandler(Attribute.GENERIC_ATTACK_DAMAGE, SharedStat.ATTACK_DAMAGE));
        handlers.put(SharedStat.ATTACK_SPEED, new AttributeStatHandler(Attribute.GENERIC_ATTACK_SPEED, SharedStat.ATTACK_SPEED));
        handlers.put(SharedStat.KNOCKBACK_RESISTANCE,
                new AttributeStatHandler(Attribute.GENERIC_KNOCKBACK_RESISTANCE, SharedStat.KNOCKBACK_RESISTANCE));
        handlers.put(SharedStat.MAX_HEALTH, new AttributeStatHandler(Attribute.GENERIC_MAX_HEALTH, SharedStat.MAX_HEALTH));

        MovementSpeedStatHandler moveSpeed = new MovementSpeedStatHandler();
        handlers.put(SharedStat.MOVEMENT_SPEED, moveSpeed);
        handlers.put(SharedStat.SPEED_MALUS_REDUCTION, moveSpeed);
    }

    /**
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
        handlers.values().forEach(update -> update.updateStatMap(map));
    }

    /**
     * Runs a specific stat update for a specific StatMap
     *
     * @param map
     *            The StatMap of the player who needs update
     * @param stat
     *            The string key of the stat which needs update
     */
    public void runUpdate(StatMap map, String stat) {
        if (handlers.containsKey(stat))
            handlers.get(stat).updateStatMap(map);
    }

    /**
     * While most RPG stats like Crit Chance, Bonus EXP have no base value,
     * others like Atk Damage (base dmg of 1) or movement speed have a base value.
     * <p>
     * The stat base value can differ from one player to another, for
     * instance Max Health which is an attribute specific to a player,
     * hence the Player argument.
     *
     * @param player The player to calculate the base stat value from
     * @param stat   The string key of the stat
     * @return The base value of this stat, 0 by default
     */
    public double getBaseValue(String stat, StatMap player) {
        return handlers.containsKey(stat) ? handlers.get(stat).getBaseStatValue(player) : 0;
    }

    /**
     * Lets MMOCore know that a specific stat needs an update whenever the
     * value of the player stat changes (due to a MMOLib stat modifier being
     * added/being removed/expiring).
     *
     * @param stat    The string key of the stat
     * @param handler The behaviour of your numeric stat
     */
    public void registerUpdate(String stat, StatHandler handler) {
        Validate.notNull(stat, "Stat cannot be null");
        Validate.notNull(handler, "Stat handler cannot be null");

        handlers.put(stat, handler);
    }
}
