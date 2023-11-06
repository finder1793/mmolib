package io.lumine.mythic.lib.api.util;

import com.google.common.collect.Lists;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemFactory {
    private static final ItemFlag[] ALL_FLAGS;
    private final ItemStack itemStack;
    private boolean clone = true;

    public static ItemFactory of(Material material) {
        return new ItemFactory(new ItemStack(material));
    }

    public static ItemFactory of(ItemStack itemStack) {
        return new ItemFactory(itemStack);
    }

    protected ItemFactory() {
        this.itemStack = new ItemStack(Material.STONE);
    }

    protected ItemFactory(ItemStack itemStack) {
        this.itemStack = (ItemStack) Objects.requireNonNull(itemStack, "itemStack");
    }

    public ItemFactory transform(Consumer<ItemStack> is) {
        is.accept(this.itemStack);
        return this;
    }

    public ItemFactory transformMeta(Consumer<ItemMeta> meta) {
        ItemMeta m = this.itemStack.getItemMeta();
        if (m != null) {
            meta.accept(m);
            this.itemStack.setItemMeta(m);
        }

        return this;
    }

    private String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public ItemFactory name(String name) {
        return this.transformMeta((meta) -> {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        });
    }

    public ItemFactory type(Material material) {
        return this.transform((itemStack) -> {
            itemStack.setType(material);
        });
    }

    public ItemFactory lore(String line) {
        return this.transformMeta((meta) -> {
            List<String> lore = meta.getLore() == null ? new ArrayList() : meta.getLore();
            ((List) lore).add(colorize(line));
            meta.setLore((List) lore);
        });
    }

    public ItemFactory lore(String... lines) {
        return this.transformMeta((meta) -> {
            List<String> lore = meta.getLore() == null ? new ArrayList() : meta.getLore();
            String[] var3 = lines;
            int var4 = lines.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String line = var3[var5];
                ((List) lore).add(colorize(line));
            }

            meta.setLore((List) lore);
        });
    }

    public ItemFactory lore(Iterable<String> lines) {
        return this.transformMeta((meta) -> {
            List<String> lore = meta.getLore() == null ? new ArrayList() : meta.getLore();
            Iterator var3 = lines.iterator();

            while (var3.hasNext()) {
                String line = (String) var3.next();
                ((List) lore).add(colorize(line));
            }

            meta.setLore((List) lore);
        });
    }

    public ItemFactory lore(Function<List<String>, Iterable<String>> linesFunc) {
        Iterable<String> lines = (Iterable) linesFunc.apply(Lists.newArrayList());
        return this.transformMeta((meta) -> {
            List<String> lore = meta.getLore() == null ? new ArrayList() : meta.getLore();
            Iterator var3 = lines.iterator();

            while (var3.hasNext()) {
                String line = (String) var3.next();
                ((List) lore).add(colorize(line));
            }

            meta.setLore((List) lore);
        });
    }

    public ItemFactory clearLore() {
        return this.transformMeta((meta) -> {
            meta.setLore(new ArrayList());
        });
    }

    public ItemFactory durability(int durability) {
        return MythicLib.plugin.getVersion().isBelowOrEqual(1, 14) ? this.transform((itemStack) -> {
            itemStack.setDurability((short) durability);
        }) : this.transformMeta((meta) -> {
            if (meta instanceof Damageable) {
                ((Damageable) meta).setDamage(durability);
            }

        });
    }

    public ItemFactory data(int data) {
        return this.durability(data);
    }

    public ItemFactory amount(int amount) {
        return this.transform((itemStack) -> {
            itemStack.setAmount(amount);
        });
    }

    public ItemFactory model(int model) {
        return this.transformMeta((meta) -> {
            meta.setCustomModelData(model);
        });
    }

    public ItemFactory enchant(Enchantment enchantment, int level) {
        return this.transform((itemStack) -> {
            if (itemStack.getType() != Material.ENCHANTED_BOOK) {
                itemStack.addUnsafeEnchantment(enchantment, level);
            } else {
                EnchantmentStorageMeta esm = (EnchantmentStorageMeta) itemStack.getItemMeta();
                esm.addStoredEnchant(enchantment, level, true);
                itemStack.setItemMeta(esm);
            }

        });
    }

    public ItemFactory enchant(Enchantment enchantment) {
        return this.enchant(enchantment, 1);
    }

    public ItemFactory clearEnchantments() {
        return this.transform((itemStack) -> {
            Set<Enchantment> var10000 = itemStack.getEnchantments().keySet();
            Objects.requireNonNull(itemStack);
            var10000.forEach(itemStack::removeEnchantment);
        });
    }

    public ItemFactory potionEffect(PotionEffectType type, int duration, int amplifier) {
        return this.transformMeta((meta) -> {
            if (meta instanceof PotionMeta) {
                PotionMeta pMeta = (PotionMeta) meta;
                PotionEffect effect = new PotionEffect(type, duration, amplifier);
                pMeta.addCustomEffect(effect, true);
            }

        });
    }

    public ItemFactory potionEffect(PotionEffectType type, int duration) {
        return this.potionEffect(type, duration, 0);
    }

    public ItemFactory clearPotionEffects() {
        return this.transformMeta((meta) -> {
            if (meta instanceof PotionMeta) {
                PotionMeta pMeta = (PotionMeta) meta;
                pMeta.clearCustomEffects();
            }

        });
    }

    public ItemFactory color(String colorCode) {
        return this.transformMeta((meta) -> {
            int r;
            int g;
            int b;
            if (colorCode.startsWith("#")) {
                Color colorx = Color.decode(colorCode);
                r = colorx.getRed();
                g = colorx.getGreen();
                b = colorx.getBlue();
            } else if (colorCode.contains(",")) {
                String[] rgb = colorCode.split(",");
                r = Integer.parseInt(rgb[0]);
                g = Integer.parseInt(rgb[1]);
                b = Integer.parseInt(rgb[2]);
            } else {
                DyeColor dColor = DyeColor.valueOf(colorCode.toUpperCase());
                org.bukkit.Color color = dColor.getFireworkColor();
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
            }

            if (meta instanceof PotionMeta) {
                PotionMeta pMeta = (PotionMeta) meta;
                pMeta.setColor(org.bukkit.Color.fromRGB(r, g, b));
            } else if (meta instanceof LeatherArmorMeta) {
                LeatherArmorMeta pMetax = (LeatherArmorMeta) meta;
                pMetax.setColor(org.bukkit.Color.fromRGB(r, g, b));
            }

        });
    }

    public ItemFactory flag(ItemFlag... flags) {
        return this.transformMeta((meta) -> {
            meta.addItemFlags(flags);
        });
    }

    public ItemFactory unflag(ItemFlag... flags) {
        return this.transformMeta((meta) -> {
            meta.removeItemFlags(flags);
        });
    }

    public ItemFactory hideAttributes() {
        if (MythicLib.plugin.getVersion().isStrictlyHigher(1, 15)) {
            this.flag(ItemFlag.HIDE_DYE);
        }

        return this.flag(ALL_FLAGS);
    }

    public ItemFactory showAttributes() {
        if (MythicLib.plugin.getVersion().isStrictlyHigher(1, 15)) {
            this.unflag(ItemFlag.HIDE_DYE);
        }

        return this.unflag(ALL_FLAGS);
    }

    public ItemFactory color(org.bukkit.Color color) {
        return this.transform((itemStack) -> {
            Material type = itemStack.getType();
            if (type == Material.LEATHER_BOOTS || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_HELMET || type == Material.LEATHER_LEGGINGS) {
                LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
                meta.setColor(color);
                itemStack.setItemMeta(meta);
            }

        });
    }

    public ItemFactory breakable(boolean flag) {
        return this.transformMeta((meta) -> {
            meta.setUnbreakable(!flag);
        });
    }

    public ItemFactory skullOwner(String owner) {
        return this.transformMeta((meta) -> {
            if (meta instanceof SkullMeta) {
                SkullMeta im = (SkullMeta) meta;
                im.setOwner(owner);
            }
        });
    }

    public ItemFactory skullTexture(String texture) {
        return this.transformMeta((meta) -> {
            if (meta instanceof SkullMeta) UtilityMethods.setTextureValue((SkullMeta) meta, texture);
        });
    }

    public ItemFactory apply(Consumer<ItemFactory> consumer) {
        consumer.accept(this);
        return this;
    }

    public ItemStack build() {
        return this.itemStack.clone();
    }

    static {
        ALL_FLAGS = new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON};
    }
}
