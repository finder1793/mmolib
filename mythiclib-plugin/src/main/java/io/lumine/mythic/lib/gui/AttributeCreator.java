package io.lumine.mythic.lib.gui;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.explorer.ChatInput;
import io.lumine.mythic.lib.api.explorer.ItemBuilder;
import io.lumine.mythic.lib.api.util.AltChar;
import io.lumine.mythic.lib.version.VersionUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AttributeCreator extends PluginInventory {
    private final AttributeExplorer explorer;

    private String namespacedKey;
    private double amount;
    private AttributeModifier.Operation operation;
    private EquipmentSlotGroup group;

    public AttributeCreator(AttributeExplorer inv) {
        super(inv.getPlayer());
        explorer = inv;
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 36, "Attribute Creation..");

        inv.setItem(10, new ItemBuilder(Material.OAK_SIGN, "&6Name-spaced Key").setLore("", "&7Current Value: &6" + (namespacedKey == null ? "None" : namespacedKey)));
        inv.setItem(11, new ItemBuilder(Material.CHEST, "&6Amount").setLore("", "&7Current Value: &6" + amount));
        inv.setItem(12, new ItemBuilder(Material.REPEATER, "&6Operation").setLore("", "&7Current Value: &6" + (operation == null ? "None" : operation.name())));

        inv.setItem(14, new ItemBuilder(Material.LEATHER_HELMET, "&6Slot Group").setLore("", "&7Current Value: &6" + (group == null ? "None" : group.toString())));

        inv.setItem(30, new ItemBuilder(Material.BARRIER, "&6" + AltChar.rightArrow + " Back"));
        inv.setItem(32, new ItemBuilder(Material.GREEN_TERRACOTTA, "&aAdd Attribute!"));

        return inv;
    }

    @Override
    public void whenClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!event.getInventory().equals(event.getClickedInventory()))
            return;

        ItemStack item = event.getCurrentItem();
        if (!UtilityMethods.isMetaItem(item))
            return;

        if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + AltChar.rightArrow + " Back"))
            explorer.open();

        else if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Add Attribute!")) {

            if (amount == 0) {
                getPlayer().sendMessage(ChatColor.RED + "> You must specify a valid amount!");
                return;
            }

            final NamespacedKey key = this.namespacedKey == null ? new NamespacedKey(MythicLib.plugin, UUID.randomUUID().toString()) : NamespacedKey.fromString(this.namespacedKey, MythicLib.plugin);
            explorer.getTarget().getAttribute(explorer.getExplored()).addModifier(VersionUtils.attrMod(key, amount, operation == null ? AttributeModifier.Operation.ADD_NUMBER : operation));
            explorer.open();
            getPlayer().sendMessage(ChatColor.YELLOW + "> Attribute successfully added.");
        }

        else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Name-spaced Key")) {
            getPlayer().closeInventory();
            getPlayer().sendMessage(ChatColor.YELLOW + "> Write in the chat the key you want. Example: `myplugin:iron_skin_pot` Type 'cancel' to abort.");
            new ChatInput(getPlayer(), (output) -> {

                if (output == null) {
                    open();
                    return true;
                }

                this.namespacedKey = output;
                getPlayer().sendMessage(ChatColor.YELLOW + "> NSK set to " + ChatColor.GOLD + namespacedKey + ChatColor.YELLOW + ".");
                open();
                return true;
            });
        }

        else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Slot Group")) {
            getPlayer().closeInventory();
            getPlayer().sendMessage(ChatColor.YELLOW + "> Write in the slot group name.");
            for (EquipmentSlot slot : EquipmentSlot.values())
                getPlayer().sendMessage(ChatColor.YELLOW + "- " + slot.name().toLowerCase());
            new ChatInput(getPlayer(), (output) -> {

                if (output == null) {
                    open();
                    return true;
                }

                String format = output.toUpperCase().replace(" ", "_").replace("-", "_");
                try {
                    group = EquipmentSlotGroup.getByName(format);
                    Validate.notNull(group, "Cannot find equipment slot group with ID '" + format + "'");
                    getPlayer().sendMessage(ChatColor.YELLOW + "> Slot set to " + ChatColor.GOLD + format + ChatColor.YELLOW + ".");
                    open();
                    return true;

                } catch (IllegalArgumentException exception) {
                    getPlayer().sendMessage(ChatColor.RED + "> " + format + " is not a valid slot name. Type 'cancel' to cancel.");
                    return false;
                }
            });
        }

        else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Operation")) {
            getPlayer().closeInventory();
            getPlayer().sendMessage(ChatColor.YELLOW + "> Write in the operation name.");
            for (AttributeModifier.Operation operation : AttributeModifier.Operation.values())
                getPlayer().sendMessage(ChatColor.YELLOW + "- " + operation.name().toLowerCase());
            new ChatInput(getPlayer(), (output) -> {

                if (output == null) {
                    open();
                    return true;
                }

                String format = output.toUpperCase().replace(" ", "_").replace("-", "_");
                try {
                    operation = AttributeModifier.Operation.valueOf(format);
                    getPlayer().sendMessage(ChatColor.YELLOW + "> Operation set to " + ChatColor.GOLD + format + ChatColor.YELLOW + ".");
                    open();
                    return true;

                } catch (IllegalArgumentException exception) {
                    getPlayer().sendMessage(ChatColor.RED + "> " + format + " is not a valid operation name. Type 'cancel' to cancel.");
                    return false;
                }
            });
        }

        else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Amount")) {
            getPlayer().closeInventory();
            getPlayer().sendMessage(ChatColor.YELLOW + "> Write in the chat the amount you want.");
            new ChatInput(getPlayer(), (output) -> {

                if (output == null) {
                    open();
                    return true;
                }

                double d;
                try {
                    d = Double.parseDouble(output);
                    amount = d;
                    getPlayer().sendMessage(ChatColor.YELLOW + "> Value set to " + ChatColor.GOLD + AttributeExplorer.FORMAT.format(d) + ChatColor.YELLOW + ".");
                    open();
                    return true;

                } catch (NumberFormatException exception) {
                    getPlayer().sendMessage(ChatColor.RED + "> " + output + " is not a valid number. Type 'cancel' to cancel.");
                    return false;
                }
            });
        }
    }
}