package io.lumine.mythic.lib.comp.adventure.resolver.implementation;

import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgumentQueue;
import io.lumine.mythic.lib.comp.adventure.resolver.AdventureTagResolver;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class VanillaColorResolver implements AdventureTagResolver {

    @Override
    public @Nullable String resolve(@NotNull String tag, @NotNull AdventureArgumentQueue argumentQueue) {
        return Arrays.stream(ChatColor.values())
                .filter(chatColor -> chatColor.name().equalsIgnoreCase(tag))
                .filter(ChatColor::isColor)
                .findFirst()
                .map(chatColor -> "%c%c".formatted(ChatColor.COLOR_CHAR, chatColor.getChar()))
                .orElse(null);
    }
}
