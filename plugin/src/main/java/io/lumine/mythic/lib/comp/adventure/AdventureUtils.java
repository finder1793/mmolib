package io.lumine.mythic.lib.comp.adventure;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
@UtilityClass
public class AdventureUtils {

    /**
     * Find a {@link ChatColor} from it's name.
     * Note: This method is case insensitive.
     *
     * @param name The name of the color.
     * @return An optional containing the color if found.
     */
    public static @NotNull Optional<ChatColor> getByName(@NotNull String name) {
        return Arrays.stream(ChatColor.values())
                .filter(chatColor -> chatColor.name().equalsIgnoreCase(name))
                .filter(ChatColor::isColor)
                .findFirst();
    }

    /**
     * Find a {@link ChatColor} from it's hexidecimal value.
     *
     * @param hex The hexidecimal value of the color as a string.
     * @return An optional containing the color if found.
     */
    public static @NotNull Optional<net.md_5.bungee.api.ChatColor> getByHex(@NotNull String hex) {
        if (hex.length() == 7 && hex.startsWith("#"))
            hex = hex.substring(1);
        // TODO: uncomment the following line
        if (hex.length() != 6 /* || MythicLib.plugin.getVersion().isBelowOrEqual(1, 15) */)
            return Optional.empty();
        try {
            return Optional.of(net.md_5.bungee.api.ChatColor.of('#' + hex));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
