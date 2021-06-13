package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.stat.StatMap;

/**
 * Stat handlers are used to handle complex behaviours for RPG numeric stats like
 * attribute based stats, which require to add an attribute modifier to the Player
 * instance.
 *
 * @author
 */
public interface StatHandler {

    /**
     * Method called when a player equips an item, when
     * he levels up... ie whenever his stat value changes.
     *
     * @param map Stat map of player being updated
     */
    public void updateStatMap(StatMap map);

    /**
     * @return The current base stat value of a given player
     */
    public double getBaseStatValue(StatMap map);
}
