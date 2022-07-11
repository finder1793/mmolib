package io.lumine.mythic.lib.api.util;

import io.lumine.mythic.lib.MythicLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class LegacyComponent {
    private static final LegacyComponentSerializer unusualSectionRGB = LegacyComponentSerializer.builder().character("§".toCharArray()[0]).hexCharacter('#').hexColors()
            .useUnusualXRepeatedCharacterHexFormat().build();

    /**
     * Used to parse text for display names by adding a white color
     * code by default if no color is added
     */
    @NotNull
    public static Component parse(String text) {
        return simpleParse(text).colorIfAbsent(NamedTextColor.WHITE);
    }

    /**
     * Used to parse lore lines (does NOT add a white color code)
     */
    @NotNull
    public static Component simpleParse(String text) {
        Component component = MiniMessage.miniMessage().deserialize(NestedSerializer.serialize(unusualSectionRGB.deserialize(MythicLib.inst().parseColors(text))));

        if (component.decorations().get(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET)
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        return component;
    }
}
