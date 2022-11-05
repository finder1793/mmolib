package io.lumine.mythic.lib.api.player;

import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.PlayerModifier;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * Used by MythicLib to make a difference between stat
 * modifiers granted by off hand and main hand items.
 * <p>
 * Used by MMOItems player inventory updates to differentiate where
 * items were placed in the player inventory.
 *
 * @author indyuce
 */
public enum EquipmentSlot {

    /**
     * Can only apply stats in armor slots
     */
    ARMOR,

    /**
     * Can't apply stats in vanilla slots
     */
    ACCESSORY,

    /**
     * Apply its stats anywhere
     */
    OTHER,

    /**
     * Apply its stats anywhere
     *
     * @deprecated Use {@link #OTHER} instead
     */
    @Deprecated
    ANY,

    /**
     * Apply stats in main hand only
     */
    MAIN_HAND,

    /**
     * Apply stats in off hand only
     */
    OFF_HAND;

    @NotNull
    public org.bukkit.inventory.EquipmentSlot toBukkit() {
        switch (this) {
            case MAIN_HAND:
                return org.bukkit.inventory.EquipmentSlot.HAND;
            case OFF_HAND:
                return org.bukkit.inventory.EquipmentSlot.OFF_HAND;
            default:
                throw new RuntimeException("Not a hand slot");
        }
    }

    private EquipmentSlot getOppositeHand() {
        Validate.isTrue(this == MAIN_HAND || this == OFF_HAND, "Not a hand equipment slot");
        return this == MAIN_HAND ? OFF_HAND : MAIN_HAND;
    }

    /**
     * Basic modifier application rule.
     *
     * @param modifier Player modifier
     * @return If a modifier should be taken into account given the action hand
     */
    public boolean isCompatible(PlayerModifier modifier) {
        return isCompatible(modifier.getSource(), modifier.getSlot());
    }

    /**
     * Every action has a player HAND associated to it, called the action hand.
     * It corresponds to the hand the the player is using to perform an action.
     * By default, MythicLib uses the Main hand if none is specified.
     * The action hand is the enum value calling this method.
     * <p>
     * Modifiers from both hands are registered in modifier maps YET filtered out when
     * calculating stat values/filtering out abilities/... Modifiers from the other hand
     * are ignored IF AND ONLY IF the other hand item is a weapon. As long as the item
     * placement is valid, non-weapon items all apply their modifiers.
     * <p>
     * Filtering out the right player modifiers is referred as "isolating modifiers"
     *
     * @param modifierSource Source of modifier
     * @param equipmentSlot  Equipment slot of modifier
     * @return If a modifier with the given equipment slot and modifier source should
     *         be taken into account given the action hand
     */
    public boolean isCompatible(ModifierSource modifierSource, EquipmentSlot equipmentSlot) {
        Validate.isTrue(isHand(), "Instance called must be a hand equipment slot");

        if (equipmentSlot == OTHER || equipmentSlot == ANY)
            return true;

        switch (modifierSource) {

            case VOID:
                return false;

            // Ignore modifiers from opposite hand if it's a weapon
            case RANGED_WEAPON:
            case MELEE_WEAPON:
                return equipmentSlot != getOppositeHand();

            // Hand items
            case OFFHAND_ITEM:
                return equipmentSlot == OFF_HAND;
            case MAINHAND_ITEM:
                return equipmentSlot == MAIN_HAND;
            case HAND_ITEM:
                return equipmentSlot.isHand();

            case ARMOR:
                return equipmentSlot == ARMOR;

            default:
                return true;
        }
    }

    public boolean isHand() {
        return this == MAIN_HAND || this == OFF_HAND;
    }

    public static EquipmentSlot fromBukkit(org.bukkit.inventory.EquipmentSlot slot) {
        switch (slot) {
            case HAND:
                return MAIN_HAND;
            case OFF_HAND:
                return OFF_HAND;
            case FEET:
            case HEAD:
            case LEGS:
            case CHEST:
                return ARMOR;
            default:
                return OTHER;
        }
    }
}