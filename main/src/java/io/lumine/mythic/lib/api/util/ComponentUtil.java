package io.lumine.mythic.lib.api.util;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.text.Component;
import io.lumine.utils.text.serializer.legacy.LegacyComponentSerializer;
import io.lumine.utils.text.serializer.plain.PlainComponentSerializer;

public class ComponentUtil {
    private static final LegacyComponentSerializer ampersandRGB = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().build();

    public static Component fromString(String text) {
        return ampersandRGB.deserialize(MythicLib.inst().parseColors(text));
    }

    public static String toPlainString(Component component) {
        return PlainComponentSerializer.plain().serialize(component);
    }
}
