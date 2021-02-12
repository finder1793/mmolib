package io.lumine.mythic.lib.api.util;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.text.Component;
import io.lumine.utils.text.minimessage.MiniMessage;
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

    // Fuck everyone who uses legacy color codes for making me do this.
    // I forgive nobody. :(
    public Component fromLegacy(String text) {
        return MiniMessage.get().deserialize(MiniMessage.get().serialize(
                ampersandRGB.deserialize(MythicLib.inst().parseColors(text))));
    }
}
