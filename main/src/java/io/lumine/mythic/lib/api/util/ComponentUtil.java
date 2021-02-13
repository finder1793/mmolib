package io.lumine.mythic.lib.api.util;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.text.Component;
import io.lumine.utils.text.minimessage.MiniMessage;
import io.lumine.utils.text.serializer.legacy.LegacyComponentSerializer;
import io.lumine.utils.text.serializer.plain.PlainComponentSerializer;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;

public class ComponentUtil {
    private static final Map<String, String> legacyColors = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    static {
        // COLOR CODES
        legacyColors.put("&0", "<black>");
        legacyColors.put("&1", "<dark_blue>");
        legacyColors.put("&2", "<dark_green>");
        legacyColors.put("&3", "<dark_aqua>");
        legacyColors.put("&4", "<dark_red>");
        legacyColors.put("&5", "<dark_purple>");
        legacyColors.put("&6", "<gold>");
        legacyColors.put("&7", "<gray>");
        legacyColors.put("&8", "<dark_gray>");
        legacyColors.put("&9", "<blue>");
        legacyColors.put("&a", "<green>");
        legacyColors.put("&b", "<aqua>");
        legacyColors.put("&c", "<red>");
        legacyColors.put("&d", "<light_purple>");
        legacyColors.put("&e", "<yellow>");
        legacyColors.put("&f", "<white>");
        // FORMATTING CODES
        legacyColors.put("&k", "<obfuscated>");
        legacyColors.put("&l", "<bold>");
        legacyColors.put("&m", "<strikethrough>");
        legacyColors.put("&n", "<underlined>");
        legacyColors.put("&o", "<italic>");
        // RESET
        legacyColors.put("&r", "<reset>");
    }

    private static final LegacyComponentSerializer ampersandRGB = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().build();
    private static final StrSubstitutor legacyColorParser = new StrSubstitutor(legacyColors);

    public static Component fromString(String text) {
        return ampersandRGB.deserialize(MythicLib.inst().parseColors(text));
    }

    public static String toPlainString(Component component) {
        return PlainComponentSerializer.plain().serialize(component);
    }

    @NotNull
    public Component legacyMiniMessage(String text) {
        return MiniMessage.get().deserialize(legacyColorParser.replace(text));
    }
}
