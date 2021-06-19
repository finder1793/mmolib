package io.lumine.mythic.lib.api.stat.modifier;

/**
 * Main problem solved by the modifier source is being able to calculate
 * specific statistics while ignoring other modifiers. When calculating the player's
 * attack damage when using a main hand weapon, MMOItems must completely ignore
 * attack damage given by off-hand modifiers.
 *
 * @author indyuce
 */
public enum ModifierSource {

    /**
     * When the modifier is added because of
     */

    OTHER,

    /**
     * Not used by MMOItems or MythicCore. Modifier granted when equipping an accessory
     */
    ACCESSORY,

    /**
     * Modifier given when equipping any armor piece.
     */
    ARMOR,

    /**
     * Modifier given when holding an item in main hand. One of the
     * two most important modifier sources, cf class definition
     */
    MAIN_HAND,

    /**
     * Modifier given when holding an item in off hand. One of the
     * two most important modifier sources, cf class definition
     */
    OFF_HAND;
}
