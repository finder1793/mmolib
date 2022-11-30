package io.lumine.mythic.lib.comp.adventure.resolver.implementation;

import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgumentQueue;
import io.lumine.mythic.lib.comp.adventure.resolver.AdventureTagResolver;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class HexColorResolver implements AdventureTagResolver {

    private static final Pattern PATTERN = Pattern.compile("<(#|HEX)([a-fA-F0-9]{6})>");

    @Override
    public @Nullable String resolve(@NotNull String src, @NotNull AdventureArgumentQueue argumentQueue) {
        Matcher match = PATTERN.matcher(src);

        while (match.find()) {
            String color = src.substring(match.start(), match.end());
            src = src.replace(color, "" + ChatColor.of('#' + match.group(2)));
            match = PATTERN.matcher(src);
        }

        return src;
    }
}
