package io.lumine.mythic.lib.api.util;

import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.format.NamedTextColor;
import io.lumine.utils.adventure.text.format.TextDecoration;
import io.lumine.utils.adventure.text.minimessage.MiniMessage;
import io.lumine.utils.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

public class LegacyComponent {
    private static final String PATTERN = "<(?:#|HEX)([a-fA-F0-9]{6})>";
    private static final LegacyComponentSerializer unusualSectionRGB = LegacyComponentSerializer.builder().character('§').hexCharacter('#').hexColors()
            .useUnusualXRepeatedCharacterHexFormat().build();

    @NotNull
    public static Component parse(String text) {
        Component component = MiniMessage.get().parse(MiniMessage.get().serialize(unusualSectionRGB.
                deserialize(ChatColor.translateAlternateColorCodes('&', text.replaceAll(PATTERN, "<#$1>")))));

        if (component.decorations().get(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET)
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        return component.colorIfAbsent(NamedTextColor.WHITE);
    }
}
