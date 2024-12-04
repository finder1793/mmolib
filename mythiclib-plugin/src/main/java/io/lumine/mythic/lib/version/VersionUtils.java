package io.lumine.mythic.lib.version;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VersionUtils {

    @NotNull
    public static AttributeModifier attrMod(@NotNull NamespacedKey key, double amount, @NotNull AttributeModifier.Operation operation) {
        return MythicLib.plugin.getVersion().getWrapper().newAttributeModifier(key, amount, operation);
    }

    private static final NamespacedKey NSK_TRICK = new NamespacedKey(MythicLib.plugin, "attr_mod_decoy");
    private static final Attribute NSK_ATTRIBUTE = Attribute.FOLLOW_RANGE;

    @NotNull
    public static AttributeModifier emptyAttributeModifier() {
        return MythicLib.plugin.getVersion().getWrapper().newAttributeModifier(NSK_TRICK, 0, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Nullable
    public static AttributeModifier getModifier(AttributeInstance instance, NamespacedKey key) {
        for (AttributeModifier modifier : instance.getModifiers())
            if (modifier.getKey().equals(key))
                return modifier;
        return null;
    }

    @NotNull
    public static void addEmptyAttributeModifier(@NotNull ItemMeta meta) {
        meta.addAttributeModifier(NSK_ATTRIBUTE, emptyAttributeModifier());
    }

    public static boolean matches(@NotNull AttributeModifier modifier, @NotNull NamespacedKey key) {
        return MythicLib.plugin.getVersion().getWrapper().matches(modifier, key);
    }

    @NotNull
    public static VInventoryView getView(@NotNull InventoryEvent event) {
        return MythicLib.plugin.getVersion().getWrapper().getView(event);
    }

    @NotNull
    public static VInventoryView getOpen(@NotNull Player player) {
        return MythicLib.plugin.getVersion().getWrapper().getOpenInventory(player);
    }

    @NotNull
    public static InventoryClickEvent invClickEvent(@NotNull VInventoryView view, @NotNull InventoryType.SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action) {
        return MythicLib.plugin.getVersion().getWrapper().newInventoryClickEvent(view, type, slot, click, action);
    }
}
