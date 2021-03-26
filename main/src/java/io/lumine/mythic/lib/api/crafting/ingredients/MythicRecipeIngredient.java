package io.lumine.mythic.lib.api.crafting.ingredients;

import org.jetbrains.annotations.NotNull;

/**
 * Different kinds of recipes require different specifications in their ingredients,
 * though they all have a few methods in common (I guess).
 *
 * @author Gunging
 */
public class MythicRecipeIngredient {

    /**
     * Actual ingredient information
     */
    @NotNull final MythicIngredient ingredient;

    /**
     * Actual ingredient information
     */
    @NotNull public MythicIngredient getIngredient() { return ingredient; }

    public MythicRecipeIngredient(@NotNull MythicIngredient ingredient) {
        this.ingredient = ingredient;
    }
}
