package io.lumine.mythic.lib.api.crafting.outputs;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.event.MythicCraftItemEvent;
import io.lumine.mythic.lib.api.crafting.ingredients.*;
import io.lumine.mythic.lib.api.crafting.recipes.MythicCachedResult;
import io.lumine.mythic.lib.api.crafting.recipes.ShapedRecipe;
import io.lumine.mythic.lib.api.crafting.recipes.vmp.VanillaInventoryMapping;
import io.lumine.mythic.lib.api.crafting.uifilters.IngredientUIFilter;
import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.util.ui.FFPMythicLib;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackCategory;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import io.lumine.utils.items.ItemFactory;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * When the player crafts something, getting another item
 * in return.
 * <p></p>
 * Except this is not necessarily one item in one slot,
 * but a set of items occupying a grid in a specific inventory.
 * <p></p>
 * Of course, usually simplified into one item on the
 * single slot of a 1-slot inventory.
 * <p></p>
 * This result creates items, it does not edit existing ones.
 * All UIFilters which don't fully define an item are ignored.
 *
 * @see ShapedRecipe#single(String, ProvidedUIFilter...) 
 * 
 * @author Gunging
 */
public class MRORecipe extends MythicRecipeOutput {

    /**
     * The recipe output of this. It is shaped because I
     * cannot think of a scenario when any of the other
     * types could fit into a single MythicRecipeInventory
     * with as much certainty as this output promises.
     */
    @NotNull final ShapedRecipe output;
    /**
     * @return The output of this MythicRecipeResult.
     */
    @NotNull public ShapedRecipe getOutput() { return output; }
    /**
     * Just the prefix displayed when there are syntax errors.
     */
    @NotNull final String recipeFFPPrefix;

    public MRORecipe(@NotNull ShapedRecipe output) {
        this.output = output;
        recipeFFPPrefix = "Mythic Recipe $u" + getOutput().getName();

        // Analyze
        analyzeDetermination();
    }

    /**
     * Is this output fully completely determinate?
     */
    boolean random = false;
    /**
     * @return May the output vary between different calls?
     */
    public boolean isRandom() { return random; }
    /**
     * Analyzes the recipe to see if this can produce random outputs.
     */
    public void analyzeDetermination() {

        // See every recipe ingredient
        for (MythicRecipeIngredient mIngredient : getOutput().getIngredients()) {

            // It must be
            ShapedIngredient shaped = (ShapedIngredient) mIngredient;

            // Ignore
            if (shaped == null) { continue; }

            // All right, get a ProvidedUIFilter to use for these co-ords
            MythicIngredient ingredient = shaped.getIngredient();

            // Ignore
            if (!ingredient.isDefinesItem()) { continue; }

            /*
             * Just examine the number of substitutes.
             *
             * Is there more than 1? Then this is random. Done.
             */
            if (ingredient.getSubstitutes().size() > 1) {
                random = true;
                determinateDisplay = null;
                return; }

            // All right then get that one ProvidedUIFilter
            ProvidedUIFilter poof = ingredient.getRandomSubstitute(false);

            // Ignore
            if (poof == null) { continue; }

            UIFilter filter = poof.getParent();

            /*
             * It is not determinate if the
             */

            // That's a success for this one
            if (filter.determinateGeneration()) { continue; }

            // Well is it determinate with these filters? Then succeed
            if (filter.partialDeterminateGeneration(poof.getArgument(), poof.getData())) { continue; }

            // Was indeterminate
            random = true;
            determinateDisplay = null;
            return;
        }


        // That's it, all of the filters are fully defined.
        random = false;
    }

    /**
     * The processed display, if this recipe is not random
     */
    @Nullable MythicRecipeInventory determinateDisplay = null;
    /**
     * Builds the determinate display to provide
     */
    @Nullable MythicRecipeInventory getDeterminateDisplay() {

        // Hey hey hey hey
        if (isRandom()) {
            determinateDisplay = null;
            return null;
        }

        // That's right yea
        if (determinateDisplay != null) { return determinateDisplay.clone(); }

        // Generate new if null
        determinateDisplay = generateDisplay();
        return determinateDisplay.clone();
    }
    /**
     * Generates a new, independent MythicRecipeInventory
     * from the recipe, with random output where possible.
     *
     * @return A new result to be displayed, <b>not the actual result</b>
     */
    @NotNull MythicRecipeInventory generateDisplay() {

        // Rows yes
        HashMap<Integer, ItemStack[]> rowsInformation = new HashMap<>();

        // Ok it doesn't exist lets build it
        for (MythicRecipeIngredient mmIngredient : getOutput().getIngredients()) {

            // Ignore
            if (mmIngredient == null) { continue; }

            // Identify Ingredient
            ShapedIngredient shaped = ((ShapedIngredient) mmIngredient);
            MythicIngredient ingredient = mmIngredient.getIngredient();

            // Does not define an item? I sleep
            if (!ingredient.isDefinesItem()) { continue; }

            // Any errors yo?
            FriendlyFeedbackProvider ffp = new FriendlyFeedbackProvider(FFPMythicLib.get());
            ffp.activatePrefix(true, recipeFFPPrefix);

            /*
             * First we must get the material of the base, a dummy
             * item basically (since this is for display) which we
             * may only display if its the only substitute of this
             * ingredient.
             *
             * If the ingredient has more substitutes, the ingredient
             * description will be used instead, replacing the meta of
             * this item entirely.
             */
            ItemStack gen = mmIngredient.getIngredient().getRandomSubstituteItem(ffp);

            // Valid?
            if (gen != null) {

                // How many substitutes do the ingredient have?
                if (ingredient.getSubstitutes().size() > 1) {

                    // Ok have to edit gen; We'll get gen's type.
                    ItemStack result = SilentNumbers.setItemName(new ItemStack(gen.getType()), SilentNumbers.findItemName(gen));

                    // Get the description from the ingredient
                    ArrayList<String> desc = new ArrayList<>();
                    for (String str : (new IngredientUIFilter().getDescription(ingredient.getName(), ""))) {

                        // Parse
                        desc.add(MythicLib.plugin.parseColors(FriendlyFeedbackProvider.quickForPlayer(FFPMythicLib.get(), str)));
                    }

                    // Replace gen
                    gen = ItemFactory.of(result).lore(desc).build();
                }

                // Get current row
                ItemStack[] row = rowsInformation.get(-shaped.getVerticalOffset());
                if (row == null) { row = new ItemStack[(shaped.getHorizontalOffset() + 1)]; }
                if (row.length < (shaped.getHorizontalOffset() + 1)) {
                    ItemStack[] newRow = new ItemStack[(shaped.getHorizontalOffset() + 1)];
                    //noinspection ManualArrayCopy
                    for (int r = 0; r < row.length; r++) { newRow[r] = row[r]; }
                    row = newRow;
                }

                // Yes
                row[shaped.getHorizontalOffset()] = gen;

                // Put
                rowsInformation.put(-shaped.getVerticalOffset(), row);

            // Log those
            } else {

                // All those invalid ones should log.
                ffp.sendTo(FriendlyFeedbackCategory.ERROR, MythicLib.plugin.getServer().getConsoleSender());
            }
        }

        // Add all rows into new
        MythicRecipeInventory ret = new MythicRecipeInventory();
        for (Integer h : rowsInformation.keySet()) { ret.setRow(h, rowsInformation.get(h)); }

        // Yes
        return ret;
    }

    /**
     * The processed result, if this recipe is not random
     */
    @Nullable MythicRecipeInventory determinateResult = null;
    /**
     * Builds the determinate result to provide
     */
    @Nullable MythicRecipeInventory getDeterminateResult() {

        // Hey hey hey hey
        if (isRandom()) {
            determinateResult = null;
            return null;
        }

        // That's right yea
        if (determinateResult != null) { return determinateResult.clone(); }

        // Generate new if null
        determinateResult = generateResult();
        return determinateResult.clone();
    }
    /**
     * Generates a new, independent MythicRecipeInventory
     * from the recipe, with random output where possible.
     *
     * @return A new result to be given to the player.
     */
    @NotNull MythicRecipeInventory generateResult() {

        // Rows yes
        HashMap<Integer, ItemStack[]> rowsInformation = new HashMap<>();

        // Ok it doesn't exist lets build it
        for (MythicRecipeIngredient mmIngredient : getOutput().getIngredients()) {

            // Ignore
            if (mmIngredient == null) { continue; }

            // Identify Ingredient
            ShapedIngredient shaped = ((ShapedIngredient) mmIngredient);
            MythicIngredient ingredient = mmIngredient.getIngredient();

            // Does not define an item? I sleep
            if (!ingredient.isDefinesItem()) { continue; }

            // Any errors yo?
            FriendlyFeedbackProvider ffp = new FriendlyFeedbackProvider(FFPMythicLib.get());
            ffp.activatePrefix(true, recipeFFPPrefix);

            /*
             * First we must get the material of the base, a dummy
             * item basically (since this is for display) which we
             * may only display if its the only substitute of this
             * ingredient.
             *
             * If the ingredient has more substitutes, the ingredient
             * description will be used instead, replacing the meta of
             * this item entirely.
             */
            ItemStack gen = mmIngredient.getIngredient().getRandomSubstituteItem(ffp);

            // Valid?
            if (gen != null) {

                // Get current row
                ItemStack[] row = rowsInformation.get(-shaped.getVerticalOffset());
                if (row == null) { row = new ItemStack[(shaped.getHorizontalOffset() + 1)]; }
                if (row.length < (shaped.getHorizontalOffset() + 1)) {
                    ItemStack[] newRow = new ItemStack[(shaped.getHorizontalOffset() + 1)];
                    //noinspection ManualArrayCopy
                    for (int r = 0; r < row.length; r++) { newRow[r] = row[r]; }
                    row = newRow;
                }

                // Yes
                row[shaped.getHorizontalOffset()] = gen;

                // Put
                rowsInformation.put(-shaped.getVerticalOffset(), row);

            // Log those
            } else {

                // All those invalid ones should log.
                ffp.sendTo(FriendlyFeedbackCategory.ERROR, MythicLib.plugin.getServer().getConsoleSender());
            }
        }

        // Add all rows into new
        MythicRecipeInventory ret = new MythicRecipeInventory();
        for (Integer h : rowsInformation.keySet()) { ret.setRow(h, rowsInformation.get(h)); }

        // Yes
        return ret;
    }

    /**
     * All right, for this recipe output to be used, we can almost guarantee
     * that the inventory and shaped recipe are of the same dimensions, but
     * otherwise, the smaller dimensions are used alv.
     *
     * @param inventory Inventory of the result, carries information on what
     *                  is already there I suppose. However, for this recipe,
     *                  it will get overwritten if an item is supposed to go
     *                  there. However there is no reason to remove other
     *                  items in undisturbed locations I guess.
     *
     * @return A built inventory for you to display.
     */
    @NotNull
    @Override
    public MythicRecipeInventory applyDisplay(@NotNull MythicRecipeInventory inventory) {
        //BBB//MythicCraftingManager.log("\u00a78MROResult \u00a7aB\u00a77 Building Display: ");
        //BBB//for (String str : inventory.toStrings("\u00a78MROResult \u00a7aC-")) { MythicCraftingManager.log(str); }

        // Attempt to get generated
        MythicRecipeInventory result = getDeterminateDisplay();

        // Null? all right build new one
        if (result == null) { result = generateDisplay(); }

        // Paste into current inventory
        MythicRecipeInventory ret = inventory.clone();

        // For every inventory slot
        for (int h = 0; h < inventory.getHeight(); h++) {
            for (int w = 0; w < inventory.getWidth(); w++) {

                // Any item in the result?
                ItemStack item = result.getItemAt(w, -h);

                // Put it yes
                ret.setItemAt(w, -h, item);

            } }

        //BBB//for (String str : ret.toStrings("\u00a78MROResult \u00a7aR-")) { MythicCraftingManager.log(str); }
        // That's it
        return ret;
    }

    @Override
    public void applyResult(@NotNull MythicRecipeInventory resultInventory, @NotNull MythicBlueprintInventory otherInventories, @NotNull MythicCachedResult cache, @NotNull InventoryClickEvent eventTrigger, @NotNull VanillaInventoryMapping map, int times) {

        //RDR//MythicCraftingManager.log("\u00a78RDR \u00a746\u00a77 Running Result Event");
        /*
         * Listen, we'll take it from here. Cancel the original event.
         *
         * Now run Mythic Crafting version of the event.
         * Did anyone cancel it? Well I guess we'll touch nothing then, but you also cant craft this item >:I
         */
        eventTrigger.setCancelled(true);
        if (times > 1 && !(eventTrigger.getWhoClicked() instanceof Player)) { return; }
        MythicCraftItemEvent preEvent = new MythicCraftItemEvent(eventTrigger, resultInventory, map, otherInventories, cache);
        Bukkit.getPluginManager().callEvent(preEvent);
        if (preEvent.isCancelled()) { return; }

        /*
         * Crafting the item only once allows to put it in the cursor.
         *
         * Otherwise, this stuff will have to
         * 1 Calculate how many times until it runs out of inventory slots
         * 2 Move it to those inventory slots
         */
        if (times == 1 && (eventTrigger.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
            /*
             * When crafting with the cursor, we must make sure that:
             *
             * 1 The player is holding nothing in the cursor
             * or
             *
             * 2 The item in their cursor is stackable with the result
             * and
             * 3 The max stacks would not be exceeded
             */
            ItemStack currentInCursor = eventTrigger.getCursor();

            /*
             * Set the result into the result slots.
             */
            //RDR//MythicCraftingManager.log("\u00a78RDR \u00a747\u00a77 Reading/Generating Result");

            // Build the result
            MythicRecipeInventory result = getDeterminateResult();
            if (result == null) { result = generateResult(); }

            //RR//for (String str : result.toStrings("\u00a78Result \u00a79RR-")) { MythicCraftingManager.log(str); }

            // Apply the result
            //RDR//MythicCraftingManager.log("\u00a78RDR \u00a748\u00a77 Processing Result Inventory");
            processInventory(resultInventory, result, times);
            //RR//for (String str : resultInventory.toStrings("\u00a78Result \u00a79PR-")) { MythicCraftingManager.log(str); }

            //RDR//MythicCraftingManager.log("\u00a78RDR \u00a749\u00a77 Finding item to put on cursor");

            // Get the zeroth entry, which will be put in the players cursor >:o
            ItemStack cursor = resultInventory.getItemAt(map.getResultWidth(eventTrigger.getSlot()), map.getResultHeight(eventTrigger.getSlot()));
            if (cursor == null) { cursor = new ItemStack(Material.AIR); }
            ItemStack actualCursor = cursor.clone();

            /*
             * All right, so, can the actual cursor stack with the current?
             */
            if (!SilentNumbers.isAir(currentInCursor)) {

                // Aye so they could stack
                if (currentInCursor.isSimilar(actualCursor)) {

                    // Exceeds max stacks?
                    int cAmount = currentInCursor.getAmount();
                    int aAmount = actualCursor.getAmount();
                    int maxAmount = actualCursor.getMaxStackSize();

                    // Cancel if their sum would exceed the max
                    if (cAmount + aAmount > maxAmount) { return; }

                    // All right recalculate amount then
                    actualCursor.setAmount(cAmount + aAmount);

                } else {

                    // Cancel this operation
                    return;
                }
            }

            //RR//MythicCraftingManager.log("\u00a78Result \u00a74C\u00a77 Found for cursor " + SilentNumbers.getItemName(actualCursor));

            // Deleting original item (now its going to be on cursor so)
            cursor.setAmount(0);

            // Apply result to the inventory
            //RDR//MythicCraftingManager.log("\u00a78RDR \u00a7410\u00a77 Actually applying to result inventory through map");
            map.applyToResultInventory(eventTrigger.getInventory(), resultInventory, false);

            // Apply result to the cursor
            eventTrigger.getView().setCursor(actualCursor);

        // Player is crafting to completion - move to inventory style.
        } else {

            /*
             * Set the result into the result slots.
             */
            //RDR//MythicCraftingManager.log("\u00a78RDR \u00a747\u00a77 Reading/Generating Result");

            // Build the result
            MythicRecipeInventory generatedResult = getDeterminateResult();
            ArrayList<ItemStack> outputItems = null;
            if (generatedResult != null) { outputItems = toItemsList(generatedResult); }
            HashMap<Integer, ItemStack> modifiedInventory = null;
            Inventory inven = eventTrigger.getWhoClicked().getInventory();
            int trueTimes = 0;

            // For every time
            for (int t = 1; t <= times; t++) {
                //RDR//MythicCraftingManager.log("\u00a78RDR \u00a748\u00a77 Iteration \u00a7c#" + t);

                // Local of these
                MythicRecipeInventory localResult = generatedResult;
                ArrayList<ItemStack> localOutput = outputItems;

                // Generate
                if (localResult == null) {

                    //RR//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Result \u00a79RR\u00a77 Indeterminate, Generated New");

                    // Generate new
                    localResult = generateResult();
                    localOutput = toItemsList(localResult); }

                //RR//for (String str : localResult.toStrings("\u00a78Result \u00a79RR-")) { io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log(str); }

                // Send to
                HashMap<Integer, ItemStack> localIterationResult = distributeInInventory(inven, localOutput, modifiedInventory);

                // Failed? Break
                if (localIterationResult == null) {

                    // No changes in the modified inventory, just break
                    //RR//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Result \u00a7cIC\u00a77 Iteration Cancelled: \u00a7cNo Inventory Space");
                    break;

                // Prepare for next iteration
                } else {

                    // Store changes
                    modifiedInventory = localIterationResult;
                    trueTimes = t;
                    //RR//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Result \u00a7aIV\u00a77 Iteration Validated, total times: \u00a7a" + trueTimes);
                } }

            // True times is 0? Cancel this.
            if (trueTimes == 0) { return; }

            // All right apply
            times = trueTimes;
            for (Integer s : modifiedInventory.keySet()) {

                // Get Item
                ItemStack putt = modifiedInventory.get(s);
                //RR//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Result \u00a79IS\u00a77 Putting \u00a7b@" + s + "\u00a77 a " + SilentNumbers.getItemName(putt));

                // Set
                inven.setItem(s, putt); }
        }

        // Consume ingredients
        consumeIngredients(otherInventories, cache, eventTrigger.getInventory(), map, times);
    }

    /**
     * @param inventory The mythic inventory to extract items from.
     *
     * @return All the ItemStacks participating in this inventory
     */
    static ArrayList<ItemStack> toItemsList(@NotNull MythicRecipeInventory inventory) {

        // Count all the different items
        ArrayList<ItemStack> ret = new ArrayList<>();
        for (int h = 0; h < inventory.getHeight(); h++) {
            for (int w = 0; w < inventory.getWidth(); w++) {

                // Get the observed
                ItemStack observed = inventory.getItemAt(w, -h);

                // Ignore null
                if (observed == null || SilentNumbers.isAir(observed)) {
                    continue;
                }

                // Include
                ret.add(observed);
            } }

        // That's it
        return ret;
    }

    /**
     * If you were to give the player all these items, what slots would they go to?
     * <p></p>
     * Supposing the player already has a few of these, this method will add up the
     * new and the old ones and present you the baked result itself, that you may
     * just do {@link Inventory#setItem(int, ItemStack)} and be done with.
     * <p></p>
     * The ItemStacks are cloned to prevent conflicts with other places.
     *
     * @param inven A <b>Player</b>'s inventory, this method only touches slots
     *              0 through 35 (The normal inventory without OFFHAND and ARMOR)
     *
     * @param contents The array of (repeatable) ItemStacks to put in the inventory.
     *
     * @param olderResult To consider the contents of this older result instead, so
     *                    that one can easily stack this method atop itself.
     *
     * @return The resultant ItemStacks that will go in each slot of the inventory.
     *         <p></p>
     *         Doesn't really apply the changes because it may fail.
     *         <p></p>
     *         <code>null</code> If the item stacks don't fit.
     */
    @Nullable HashMap<Integer, ItemStack> distributeInInventory(@NotNull Inventory inven, @NotNull ArrayList<ItemStack> contents, @Nullable HashMap<Integer, ItemStack> olderResult) {

        // Organize
        ArrayList<ItemStack> out = notRepeated(contents);
        //HashMap<ItemStack, Integer> excess = new HashMap<>();

        // Build
        HashMap<Integer, ItemStack> result = new HashMap<>();
        HashMap<Integer, ItemStack> invenItems = new HashMap<>();
        if (olderResult == null) { olderResult = new HashMap<>(); }
        for (int s = 0; s < 36; s++) {
            ItemStack i = olderResult.get(s);
            if (!SilentNumbers.isAir(i)) {
                result.put(s, i);
                invenItems.put(s, i);
                //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79EO\u00a77 Overrode with previous result " + SilentNumbers.getItemName(i));
            } else {  invenItems.put(s, inven.getItem(s)); }
        }

        /*
         * Now check the player's inventory. Some slots may be completely available
         * which means that they have nothing in them, other slots might have the
         * item we are crafting right now.
         *
         * Filled in 2 steps:
         *
         * Uno - Stack stackable items
         *
         * Dos - Remainder in empty slots
         */
        int completelyResolved = 0;
        //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79E\u00a77 Filling existing stacks...");
        for (int s = 0; s < 36; s++) {

            // No need to continue
            if (completelyResolved >= out.size()) {
                //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79E\u00a77 All completely resolved");
                break; }

            // What empty slots are there?
            ItemStack i = invenItems.get(s);
            //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79E\u00a77 Slot \u00a73@" + s + " " + SilentNumbers.getItemName(i));

            // Not air?
            if (!SilentNumbers.isAir(i)) {

                // Which to put...
                for (ItemStack itm : out) {

                    // Stackable, and not full stack?
                    if (itm.isSimilar(i) && (itm.getType().getMaxStackSize() > i.getAmount())) {
                        //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79EF\u00a77 Found to distribute " + SilentNumbers.getItemName(itm));

                        // Fill inventory slot
                        int iCurrent = i.getAmount();
                        int iAdding = itm.getAmount();
                        int iMaxAdd = itm.getType().getMaxStackSize() - iCurrent;
                        int iAdded = Math.min(iMaxAdd, iAdding);
                        int iRem = iAdding - iAdded;

                        // All right that's what you put in there
                        ItemStack put = asAmount(i, iCurrent + iAdded);
                        //ItemStack remainder = asAmount(i, iRem);
                        itm.setAmount(iRem);

                        // Store result
                        result.put(s, put);
                        if (iRem == 0) { completelyResolved++; }
                        //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79EF\u00a77 Put in slot " + SilentNumbers.getItemName(put) + "\u00a77, Remainder: " + SilentNumbers.getItemName(itm));
                        break;

                        /*/ Merge in remainder
                        boolean merged = false;
                        for (Map.Entry<ItemStack, Integer> ent : excess.entrySet()) {

                            // Entry matches?
                            if (ent.getKey().isSimilar(remainder)) {

                                // Add that amount
                                merged = true;
                                ent.setValue(ent.getValue() + remainder.getAmount());
                                break;
                            }
                        }

                        // Not contained? Add
                        if (!merged) {

                            // Register yes
                            excess.put(remainder, remainder.getAmount());
                        } //*/
                    }
                }
            }
        }

        // Re-Stacc
        out = notRepeated(out); int o = (out.size() - 1);

        //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79F\u00a77 Filling empty slots...");
        for (int s = 0; s < 36 && o >= 0; s++) {

            // No need to continue
            if (completelyResolved == out.size()) {
                o = -1;
                //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79E\u00a77 All completely resolved");
                break; }

            // What empty slots are there?
            ItemStack i = invenItems.get(s);
            //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79F\u00a77 Slot \u00a73@" + s + " " + SilentNumbers.getItemName(i));

            // Not air?
            if (SilentNumbers.isAir(i)) {
                //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a7aF-O\u00a77 Air");

                /*
                 * So now this slot is completely empty and available.
                 */
                ItemStack oStack = out.get(o);
                if (oStack.getAmount() > 0) {
                    //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a79FP\u00a77 Put " + SilentNumbers.getItemName(oStack) + "\u00a77, Unaddressed: \u00a7b" + o);
                    result.put(s, oStack.clone());
                    completelyResolved++; }

                o--;
            }
            //DS//else { io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a7cF-O\u00a77 Not Air"); }
        }

        // Success? (o depleted)
        if (o >= 0) {
            //DS//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78Distribute \u00a74FC\u00a77 No capacity, left unaddressed: \u00a7b" + o);
            return null; }

        return result;
    }

    /**
     * @param contents 'Repeatable' ItemStacks, will ignore those that are null or AIR
     *
     * @return Them re-ordered so that they are completely stacked (up to, obviously,
     *         the max stack amount).
     *         <p></p>
     *         This will effectively combine stacks, but will end up splitting them
     *         anyway if they surpass their {@link Material#getMaxStackSize()}.
     */
    @NotNull ArrayList<ItemStack> notRepeated(@NotNull ArrayList<ItemStack> contents) {

        // Fill
        ArrayList<ItemStack> ret = new ArrayList<>();
        for (ItemStack itm : contents) {

            // Skill null and air
            if (SilentNumbers.isAir(itm)) { continue; }

            // Any of th current already stacks?
            boolean stacked = false;
            for (int c = 0; c < ret.size(); c++) {

                // Get that one
                ItemStack comp = ret.get(c);
                Material mat = comp.getType();

                // Similar and still can stack?
                if (comp.isSimilar(itm) && comp.getAmount() < mat.getMaxStackSize()) {

                    // Remove
                    ret.remove(c);

                    // Add totals
                    int current = comp.getAmount();
                    int added = itm.getAmount();
                    int total = current + added;

                    // Greater than? Split
                    while (total > mat.getMaxStackSize()) {

                        // Add
                        ret.add(asAmount(comp, mat.getMaxStackSize()));

                        // Subtract
                        total -= mat.getMaxStackSize(); }

                    // Add remainder
                    if (total > 0) {

                        // Add yes
                        ret.add(asAmount(comp, total)); }

                    // No need to keep searching for similar stack to stack
                    stacked = true;
                    break;
                } }

            // None similar?
            if (!stacked) {

                // Add normally
                ret.add(itm);
            }
        }

        // That's it
        return ret;
    }

    /**
     * @param comp What ItemStack
     *
     * @param amount How many
     *
     * @return Cloned and as the desired amount
     */
    @NotNull ItemStack asAmount(@NotNull ItemStack comp, int amount) {
        ItemStack s = comp.clone();
        s.setAmount(amount);
        return s;
    }
}
