package io.lumine.mythic.lib.api.crafting.recipes.vmp;

import io.lumine.mythic.lib.api.crafting.recipes.MythicRecipeStation;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * The Smithing Station, with:
 * - a slot of a template
 * - a slot for armor/tool
 * - a slot for an ingot/material
 * - a result slot
 *
 * @author Gunging
 */
@Deprecated
public class ModernSmithingStationMapping extends VanillaInventoryMapping {

    private static final int
            TEMPLATE_SLOT = 0,
            MAIN_SLOT = 1,
            INGOT_SLOT = 2,
            RESULT_SLOT = 3;

    //main inv

    @Override
    public int getMainWidth(int slot) throws IllegalArgumentException {
        if (slot == MAIN_SLOT) return 0;
        throw outOfBounds(slot);
    }

    @Override
    public int getMainHeight(int slot) throws IllegalArgumentException {
        if (slot == MAIN_SLOT) return 0;
        throw outOfBounds(slot);
    }

    @Override
    public int getMainSlot(int width, int height) throws IllegalArgumentException {
        if (height == 0 && width == 0) return MAIN_SLOT;
        throw outOfBounds(width, height);
    }

    @Override
    public int getMainInventoryStart() {
        return MAIN_SLOT;
    }

    @Override
    public int getMainInventoryWidth() {
        return 1;
    }

    @Override
    public int getMainInventoryHeight() {
        return 1;
    }

    //result

    @Override
    public int getResultWidth(int slot) throws IllegalArgumentException {
        if (slot == RESULT_SLOT) return 0;
        throw outOfBounds(slot);
    }

    @Override
    public int getResultHeight(int slot) throws IllegalArgumentException {
        if (slot == RESULT_SLOT) return 0;
        throw outOfBounds(slot);
    }

    @Override
    public int getResultSlot(int width, int height) throws IllegalArgumentException {
        if (width == 0 && height == 0) return RESULT_SLOT;
        throw outOfBounds(width, height);
    }

    @Override
    public int getResultInventoryStart() {
        return RESULT_SLOT;
    }

    @Override
    public int getResultInventoryWidth() {
        return 1;
    }

    @Override
    public int getResultInventoryHeight() {
        return 1;
    }

    @Override
    public boolean isResultSlot(int slot) {
        return slot == RESULT_SLOT;
    }

    //side

    @Override
    public int getSideWidth(@NotNull String side, int slot) throws IllegalArgumentException {
        validateSide(side);
        if (slot == 1 || slot == 2) return 0;
        throw outOfBounds(slot);
    }

    @Override
    public int getSideHeight(@NotNull String side, int slot) throws IllegalArgumentException {
        validateSide(side);
        if (slot == 1 || slot == 2) return 0;
        throw outOfBounds(slot);
    }

    @Override
    public int getSideSlot(@NotNull String side, int width, int height) throws IllegalArgumentException {
        validateSide(side);
        if (width == 0 && height == 0) return side.equals("template") ? TEMPLATE_SLOT : INGOT_SLOT;
        throw outOfBounds(width, height);
    }

    @Override
    public int getSideInventoryStart(@NotNull String side) throws IllegalArgumentException {
        validateSide(side);
        return side.equals("template") ? TEMPLATE_SLOT : INGOT_SLOT;
    }

    @Override
    public int getSideInventoryWidth(@NotNull String side) throws IllegalArgumentException {
        validateSide(side);
        return 1;
    }

    @Override
    public int getSideInventoryHeight(@NotNull String side) throws IllegalArgumentException {
        validateSide(side);
        return 1;
    }

    @Override
    public boolean mainIsResult() {
        return false;
    }

    @NotNull
    final static ArrayList<String> sNames = SilentNumbers.toArrayList("template", "ingot");

    @NotNull
    @Override
    public ArrayList<String> getSideInventoryNames() {
        return sNames;
    }

    @NotNull
    @Override
    public InventoryType getIntendedInventory() {
        return InventoryType.SMITHING;
    }

    @Nullable
    @Override
    public MythicRecipeStation getIntendedStation() {
        return MythicRecipeStation.SMITHING;
    }
}
