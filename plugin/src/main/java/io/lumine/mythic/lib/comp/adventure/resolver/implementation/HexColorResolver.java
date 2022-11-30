package io.lumine.mythic.lib.comp.adventure.resolver.implementation;

import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgumentQueue;
import io.lumine.mythic.lib.comp.adventure.resolver.AdventureTagResolver;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class HexColorResolver implements AdventureTagResolver {

    @Override
    public @Nullable String resolve(@NotNull String tag, @NotNull AdventureArgumentQueue args) {
        if (!args.hasNext())
            return null;
        try {
            return "" + ChatColor.of('#' + args.pop().value());
        } catch (Exception e) {
            return null;
        }
    }
}
