package io.lumine.mythic.lib.version;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class VersionUtils {

    @NotNull
    public static AttributeModifier attrMod(@NotNull NamespacedKey key, double amount, @NotNull AttributeModifier.Operation operation) {
        return MythicLib.plugin.getVersion().getWrapper().newAttributeModifier(key, amount, operation);
    }

    public static boolean matches(@NotNull AttributeModifier modifier, @NotNull NamespacedKey key) {
        return MythicLib.plugin.getVersion().getWrapper().matches(modifier, key);
    }
}
