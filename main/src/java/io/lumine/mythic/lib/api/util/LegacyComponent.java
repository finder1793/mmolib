package io.lumine.mythic.lib.api.util;

import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.format.NamedTextColor;
import io.lumine.utils.adventure.text.format.TextDecoration;
import io.lumine.utils.adventure.text.minimessage.MiniMessage;

import io.lumine.utils.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;

public class LegacyComponent {
    private static final String PATTERN = "<(?:#|HEX)([a-fA-F0-9]{6})>";

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
            .character(LegacyComponentSerializer.SECTION_CHAR).hexCharacter('#')
            .hexColors().useUnusualXRepeatedCharacterHexFormat().build();

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

        /*
         * Work Order:
         * 1. Uses the pattern to change alternate hex formats to the appropriate
         * minimessage format. The reset is to emulate legacy behavior.
         *
         * 2. Uses a custom legacy serializer to catch already parsed bukkit
         * ChatColors and deserialize the whole thing to a component.
         *
         * 3. The component is then serialized into a minimessage acceptable string.
         *
         * 4. Minimessage then parses the string into the final component.
         *
         * 5. Sets to not be italic unless it is stated.
         */
        return Component.text()
                .append(MiniMessage.get().parse(MiniMessage.get().serialize(
                        SERIALIZER.deserialize(text.replaceAll(PATTERN, "<reset><#$1>")))))
                .decoration(TextDecoration.ITALIC, false).colorIfAbsent(NamedTextColor.WHITE)
                .build(); }
}
