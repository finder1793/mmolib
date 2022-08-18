package io.lumine.mythic.lib.comp.hexcolor;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexColorParser implements ColorParser {
    private static final Pattern PATTERN = Pattern.compile("<(#|HEX)([a-fA-F0-9]{6})>");

    @Override
    @Contract("null -> null")
    @Nullable
    public String parseColorCodes(@Nullable String format) {
        if (format == null) { return null; }

        Matcher match = PATTERN.matcher(format);

        while (match.find()) {
            String color = format.substring(match.start(), match.end());
            format = format.replace(color, "" + ChatColor.of('#' + match.group(2)));
            match = PATTERN.matcher(format);
        }

        return ChatColor.translateAlternateColorCodes('&', format);
    }
}
