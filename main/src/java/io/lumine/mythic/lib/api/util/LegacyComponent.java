package io.lumine.mythic.lib.api.util;

import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.minimessage.MiniMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;

public class LegacyComponent {
    private static final String PATTERN = "<(?:#|HEX)([a-fA-F0-9]{6})>";
    private static final Map<String, String> legacyColors = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    static {
        // The resets are created to imitate legacy behavior, not because I am a psychopath.
        // COLOR CODES
        legacyColors.put("&0", "<reset><black>");
        legacyColors.put("&1", "<reset><dark_blue>");
        legacyColors.put("&2", "<reset><dark_green>");
        legacyColors.put("&3", "<reset><dark_aqua>");
        legacyColors.put("&4", "<reset><dark_red>");
        legacyColors.put("&5", "<reset><dark_purple>");
        legacyColors.put("&6", "<reset><gold>");
        legacyColors.put("&7", "<reset><gray>");
        legacyColors.put("&8", "<reset><dark_gray>");
        legacyColors.put("&9", "<reset><blue>");
        legacyColors.put("&a", "<reset><green>");
        legacyColors.put("&b", "<reset><aqua>");
        legacyColors.put("&c", "<reset><red>");
        legacyColors.put("&d", "<reset><light_purple>");
        legacyColors.put("&e", "<reset><yellow>");
        legacyColors.put("&f", "<reset><white>");
        // FORMATTING CODES
        legacyColors.put("&k", "<obfuscated>");
        legacyColors.put("&l", "<bold>");
        legacyColors.put("&m", "<strikethrough>");
        legacyColors.put("&n", "<underlined>");
        legacyColors.put("&o", "<italic>");
        // RESET
        legacyColors.put("&r", "<reset>");
    }

    @NotNull
    public static Component parse(String text) {
        for (Map.Entry<String,String> entry : legacyColors.entrySet()){
             text = text.replace(entry.getKey(), entry.getValue());
        }
        return MiniMessage.get().parse(text.replaceAll(PATTERN, "<reset><#$1>"));
    }
}
