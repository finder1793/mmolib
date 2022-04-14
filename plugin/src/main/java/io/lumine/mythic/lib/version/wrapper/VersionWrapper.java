package io.lumine.mythic.lib.version.wrapper;

import io.lumine.mythic.lib.api.MMORayTraceResult;
import io.lumine.mythic.lib.api.item.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.function.Predicate;

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

    /**
     * @see {@link io.lumine.mythic.lib.util.RayTrace}
     * @deprecated No longer depends on the server version.
     */
    @Deprecated
    default MMORayTraceResult rayTrace(Player player, double range, Predicate<Entity> predicate) {
        return rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), range, predicate);
    }

    /**
     * @see {@link io.lumine.mythic.lib.util.RayTrace}
     * @deprecated No longer depends on the server version.
     */
    @Deprecated
    default MMORayTraceResult rayTrace(Player player, Vector direction, double range, Predicate<Entity> predicate) {
        return rayTrace(player.getEyeLocation(), direction, range, predicate);
    }

    /**
     * @see {@link io.lumine.mythic.lib.util.RayTrace}
     * @deprecated No longer depends on the server version.
     */
    @Deprecated
    MMORayTraceResult rayTrace(Location loc, Vector direction, double range, Predicate<Entity> predicate);

    /**
     * used by MMOItems to update an item's durability bar in 1.12. in 1.12 you
     * need to update the item durability using the ItemStack instance, in 1.13+
     * you need to manipulate the itemMeta
     */
    @Deprecated
    void applyDurabilityData(ItemStack item, ItemStack data);

    NBTItem copyTexture(NBTItem item);

    ItemStack textureItem(Material material, int model);

    @Deprecated
    BossBar createBossBar(NamespacedKey key, String title, BarColor color, BarStyle style, BarFlag... flags);

    Enchantment getEnchantmentFromString(String s);

    FurnaceRecipe getFurnaceRecipe(NamespacedKey key, ItemStack item, Material material, float exp, int cook);

    boolean isCropFullyGrown(Block block);

    boolean isUndead(Entity entity);

    // Mostly NMS based methods from here
    NBTItem getNBTItem(ItemStack item);

    void sendActionBar(Player player, String message);

    void sendJson(Player player, String message);

    int getNextContainerId(Player player);

    void handleInventoryCloseEvent(Player player);

    void sendPacketOpenWindow(Player player, int containerId);

    void sendPacketCloseWindow(Player player, int containerId);

    void setActiveContainerDefault(Player player);

    void setActiveContainer(Player player, Object container);

    void setActiveContainerId(Object container, int containerId);

    void addActiveContainerSlotListener(Object container, Player player);

    void playArmAnimation(Player player);

    Inventory toBukkitInventory(Object container);

    Sound getBlockPlaceSound(Block block);

    Object newContainerAnvil(Player player);

    @Deprecated
    boolean isInBoundingBox(Entity entity, Location loc);

    @Deprecated
    double distanceSquaredFromBoundingBox(Entity entity, Location loc);

    // What the hecky-smecky was this supposed to be?
	/*default double distanceFromBoundingBox(Entity entity, Location loc) {
		return Math.sqrt(distanceFromBoundingBox(entity, loc));
	}*/

    String getSkullValue(Block block);

    void setSkullValue(Block block, String value);
}
