package io.lumine.mythic.lib.api.util;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.text.Component;
import io.lumine.utils.text.serializer.legacy.LegacyComponentSerializer;
import io.lumine.utils.text.serializer.plain.PlainComponentSerializer;

public class ComponentUtil {
    public Component fromString(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(MythicLib.inst().parseColors(text));
    }

    public static String toString(Component component) {
        return PlainComponentSerializer.plain().serialize(component);
    }
}
