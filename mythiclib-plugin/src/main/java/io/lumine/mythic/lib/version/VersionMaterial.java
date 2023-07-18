package io.lumine.mythic.lib.version;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum VersionMaterial {
    OAK_SIGN("SIGN", "SIGN"),
    LAPIS_LAZULI("LAPIS_LAZULI", "INK_SACK", 4),
    LIME_DYE("LIME_DYE", "INK_SACK", 5),
    LIGHT_GRAY_DYE("LIGHT_GRAY_DYE", "INK_SACK", 7),
    GRAY_DYE("GRAY_DYE", "INK_SACK", 8),
    LIGHT_BLUE_DYE("LIGHT_BLUE_DYE", "INK_SACK", 12),
    RED_DYE("ROSE_RED", "INK_SACK", 14),
    BONE_MEAL("BONE_MEAL", "INK_SACK", 18),
    GRAY_STAINED_GLASS_PANE("GRAY_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 7),
    RED_STAINED_GLASS_PANE("RED_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 14),
    GREEN_STAINED_GLASS_PANE("GREEN_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 13),
    LIME_STAINED_GLASS("LIME_STAINED_GLASS", "STAINED_GLASS", 5),
    PINK_STAINED_GLASS("PINK_STAINED_GLASS", "STAINED_GLASS", 6),
    PLAYER_HEAD("PLAYER_HEAD", "SKULL_ITEM", 3),
    SKELETON_SKULL("SKELETON_SKULL", "SKULL_ITEM"),
    NETHER_WART("NETHER_WART", "NETHER_STALK"),
    WRITABLE_BOOK("WRITABLE_BOOK", "BOOK_AND_QUILL"),
    CRAFTING_TABLE("CRAFTING_TABLE", "WORKBENCH"),
    SNOWBALL("SNOWBALL", "SNOW_BALL"),
    LILY_PAD("LILY_PAD", "WATER_LILY"),
    GUNPOWDER("GUNPOWDER", "SULPHUR"),
    OAK_SAPLING("OAK_SAPLING", "SAPLING"),
    COMPARATOR("COMPARATOR", "REDSTONE_COMPARATOR"),
    EXPERIENCE_BOTTLE("EXPERIENCE_BOTTLE", "EXP_BOTTLE"),
    IRON_HORSE_ARMOR("IRON_HORSE_ARMOR", "IRON_BARDING"),
    MUSIC_DISC_MALL("MUSIC_DISC_MALL", "RECORD_8"),
    COBBLESTONE_WALL("COBBLESTONE_WALL", "COBBLE_WALL"),
    ENDER_EYE("ENDER_EYE", "EYE_OF_ENDER"),
    GRASS_BLOCK("GRASS_BLOCK", "GRASS"),
    ENCHANTING_TABLE("ENCHANTING_TABLE", "ENCHANTMENT_TABLE"),
    PORKCHOP("PORKCHOP", "PORK"),
    GOLDEN_CHESTPLATE("GOLDEN_CHESTPLATE", "GOLD_CHESTPLATE"),
    GOLDEN_HORSE_ARMOR("GOLDEN_HORSE_ARMOR", "GOLD_BARDING"),
    COMMAND_BLOCK_MINECART("COMMAND_BLOCK_MINECART", "COMMAND_MINECART"),
    COMMAND_BLOCK("COMMAND_BLOCK", "COMMAND"),
    OAK_PLANKS("OAK_PLANKS", "WOOD"),
    SPRUCE_PLANKS("SPRUCE_PLANKS", "WOOD", 1),
    BIRCH_PLANKS("BIRCH_PLANKS", "WOOD", 2),
    JUNGLE_PLANKS("JUNGLE_PLANKS", "WOOD", 3),
    ACACIA_PLANKS("ACACIA_PLANKS", "WOOD", 4),
    DARK_OAK_PLANKS("DARK_OAK_PLANKS", "WOOD", 5),
    //todo CRIMSON_PLANKS("CRIMSON_PLANKS", "WOOD"),
    //todo WARPED_PLANKS("WARPED_PLANKS", "WOOD"),
    CAULDRON("CAULDRON", "CAULDRON_ITEM"),
    DIAMOND_HORSE_ARMOR("DIAMOND_HORSE_ARMOR", "DIAMOND_BARDING"),
    MAGENTA_DYE("MAGENTA_DYE", "INK_SACK", 13),
    PLAYER_WALL_HEAD("PLAYER_WALL_HEAD", "SKULL_ITEM", 3),
    WITHER_SKELETON_SKULL("WITHER_SKELETON_SKULL", "SKULL_ITEM", 1),
    FIRE_CHARGE("FIRE_CHARGE", "FIREBALL"),
    TOTEM_OF_UNDYING("TOTEM_OF_UNDYING", "TOTEM"),

    BLAST_FURNACE("FURNACE", "FURNACE", "FURNACE"),
    CAMPFIRE("FURNACE", "FURNACE", "FURNACE"),
    LEATHER_HORSE_ARMOR("LEATHER_HORSE_ARMOR", "LEATHER_HELMET", "LEATHER_HELMET"),
    SMOKER("FURNACE", "FURNACE", "FURNACE"),
    SMITHING_TABLE("FURNACE", "FURNACE", "FURNACE");

    private final ItemStack item;

    VersionMaterial(String name_1_13, String legacy) {
        this(name_1_13, legacy, 0);
    }

    /**
     * Field name corresponds to 1.14+ material name
     *
     * @param name_1_13
     *            1.13 name of the material
     * @param legacy
     *            Legacy material name
     * @param legacyDurability
     *            Legacy material durability
     */
    @SuppressWarnings("deprecation")
    VersionMaterial(String name_1_13, String legacy, int legacyDurability) {
        item = MythicLib.plugin.getVersion().isStrictlyHigher(1, 12)
                ? new ItemStack(Material.valueOf(MythicLib.plugin.getVersion().isStrictlyHigher(1, 13) ? name() : name_1_13))
                : new ItemStack(Material.valueOf(legacy), 1, (short) legacyDurability);
    }

    /**
     * Field name corresponds to 1.15+ material name
     *
     * @param name_1_14
     *            1.14 name of the material
     * @param name_1_13
     *            1.13 name of the material
     * @param legacy
     *            Legacy material name with no durability
     */
    VersionMaterial(String name_1_14, String name_1_13, String legacy) {
        item = new ItemStack(Material.valueOf(MythicLib.plugin.getVersion().isStrictlyHigher(1, 14) ? name()
                : MythicLib.plugin.getVersion().isStrictlyHigher(1, 13) ? name_1_14
                : MythicLib.plugin.getVersion().isStrictlyHigher(1, 12) ? name_1_13 : legacy));
    }

    public Material toMaterial() {
        return item.getType();
    }

    public ItemStack toItem() {
        return item.clone();
    }
}
