package io.lumine.mythic.lib.api.util;

import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.format.TextDecoration;
import io.lumine.utils.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

public class LegacyComponent {
    private static final String PATTERN = "<(?:#|HEX)([a-fA-F0-9]{6})>";
    @NotNull
    public static Component parse(String text) {
        Component component = MiniMessage.get().parse(ChatColor
                .translateAlternateColorCodes('&', text.replaceAll(PATTERN, "<#$1>")));

        if(component.decorations().get(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET && component.color() != null)
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        return component;
    }
}
