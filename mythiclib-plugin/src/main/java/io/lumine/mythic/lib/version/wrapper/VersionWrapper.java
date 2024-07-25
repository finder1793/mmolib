package io.lumine.mythic.lib.version.wrapper;

import com.mojang.authlib.GameProfile;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.version.OreDrops;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface VersionWrapper {

    static final String PLAYER_PROFILE_NAME = "SkullTexture";

    /**
     * @return Either a GameProfile object or PlayerProfile depending on version.
     */
    Object getProfile(SkullMeta meta);

    /**
     * This takes in either a GameProfile object or PlayerProfile object and applies it
     * to the target skull meta depending on server version.
     */
    void setProfile(SkullMeta meta, Object object);

    /**
     * Spigot 1.20 introduced an API method to manipulate skull textures
     * without having to rely on reflection. Previous versions still
     * rely on reflection while newer versions can switch to the API.
     * <p>
     * The new PlayerProfile API requires to both support PlayerProfile
     * and GameProfile objects as reflection is no longer supported by >1.20.2
     */
    Object newProfile(UUID uniqueId, String textureValue);

    /**
     * Used by MMOItems to check if a block can be autosmelt. Also
     * used to apply Fortune levels for loot multiplication.
     *
     * @param material Type of broken block
     * @return Drops of provided block if it's an ore, null otherwise
     */
    OreDrops getOreDrops(Material material);

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

    void setUUID(Player player, UUID uniqueId);

    GameProfile getGameProfile(Player player);

    default AttributeModifier newAttributeModifier(@NotNull NamespacedKey key, double amount, @NotNull AttributeModifier.Operation operation) {
        return new AttributeModifier(key.toString(), amount, operation);
    }

    default boolean matches(AttributeModifier modifier, NamespacedKey key) {
        return modifier.getName().equals(key.toString());
    }
}
