package io.lumine.mythic.lib.api.util;

import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

public class LegacyComponent {
    private static final String PATTERN = "<(?:#|HEX)([a-fA-F0-9]{6})>";

    @NotNull
    public static Component parse(String text) {
        return MiniMessage.get().parse(ChatColor.translateAlternateColorCodes('&', text.replaceAll(PATTERN, "<#$1>")));
    }
}
