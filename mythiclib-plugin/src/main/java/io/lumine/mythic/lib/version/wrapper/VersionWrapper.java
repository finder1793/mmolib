package io.lumine.mythic.lib.version.wrapper;

import io.lumine.mythic.lib.api.item.NBTItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface VersionWrapper {

    /**
     * Used by MMOItems to check if a block can be autosmelt.
     *
     * @return Map that maps to autosmeltable ores the corresponding drop
     */
    Map<Material, Material> getOreDrops();

    /**
     * Used by MMOItems when eating consumables the vanilla way. There is an
     * issue where when eating items, the saturation and food buff still applies
     * which MMOItems need to take an offset of.
     *
     * @param item Has to be a food item
     * @return Food restored when eating
     * @throws NullPointerException If it is not a food item
     */
    int getFoodRestored(ItemStack item);

    /**
     * This is necessary because Bukkit 1.14 don't have
     * such a method which was introduced in Bukkit 1.15
     *
     * @return Between 0 and 1, 1 being a fully charged attack
     */
    float getAttackCooldown(Player player);

    /**
     * Used by MMOCore to check if a block has been generated by a
     * cobblestone or obsidian generator.
     *
     * @param material Block material
     */
    boolean isGeneratorOutput(Material material);

    /**
     * Used by MMOItems when eating consumables the vanilla way. There is an
     * issue where when eating items, the saturation and food buff still applies
     * which MMOItems need to take an offset of.
     *
     * @param item Has to be a food item
     * @return Saturation restored when eating
     * @throws NullPointerException If it is not a food item
     */
    float getSaturationRestored(ItemStack item);

    FurnaceRecipe getFurnaceRecipe(String path, ItemStack item, Material material, float exp, int cook);

    NBTItem copyTexture(NBTItem item);

    ItemStack textureItem(Material material, int model);

    Enchantment getEnchantmentFromString(String s);

    FurnaceRecipe getFurnaceRecipe(NamespacedKey key, ItemStack item, Material material, float exp, int cook);

    boolean isCropFullyGrown(Block block);

    boolean isUndead(Entity entity);

    // Mostly NMS based methods from here
    NBTItem getNBTItem(ItemStack item);

    boolean isHelmet(Material material);

    default void sendActionBar(Player player, String message) {
        sendActionBarRaw(player, "{\"text\": \"" + message + "\"}");
    }

    void sendActionBarRaw(Player player, String message);

    /**
     * Sends a raw chat message
     */
    void sendJson(Player player, String message);

    @Deprecated
    int getNextContainerId(Player player);

    @Deprecated
    void handleInventoryCloseEvent(Player player);

    @Deprecated
    void sendPacketOpenWindow(Player player, int containerId);

    @Deprecated
    void sendPacketCloseWindow(Player player, int containerId);

    @Deprecated
    void setActiveContainerDefault(Player player);

    @Deprecated
    void setActiveContainer(Player player, Object container);

    @Deprecated
    void setActiveContainerId(Object container, int containerId);

    @Deprecated
    void addActiveContainerSlotListener(Object container, Player player);

    @Deprecated
    Inventory toBukkitInventory(Object container);

    @Deprecated
    Object newContainerAnvil(Player player);

    void playArmAnimation(Player player);

    Sound getBlockPlaceSound(Block block);

    String getSkullValue(Block block);

    void setSkullValue(Block block, String value);
}