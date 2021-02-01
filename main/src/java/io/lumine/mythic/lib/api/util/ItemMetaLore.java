package io.lumine.mythic.lib.api.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemMetaLore {
    @Deprecated
    public static ItemStack addLoreLines(ItemStack item, List<String> lore) {
        try {
            NBTItem nbtItem = NBTItem.get(item);
            List<BaseComponent> components = nbtItem.getLoreComponents();
            for (String line : lore) {
                components.add(TextComponent.fromLegacyText(line, ChatColor.WHITE)[0]);
            }
            nbtItem.setLoreComponents(components);
            return nbtItem.toItem();
        } catch (NullPointerException e) {
            return item;
        }
    }
    @Deprecated
    public static ItemStack removeLoreLines(ItemStack item, int amount) {
        try {
            NBTItem nbtItem = NBTItem.get(item);
            List<BaseComponent> components = nbtItem.getLoreComponents();
            nbtItem.setLoreComponents(components.subList(0, components.size() - amount));
            return nbtItem.toItem();
        } catch (NullPointerException e) {
            return item;
        }

    }
}
