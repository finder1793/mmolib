package io.lumine.mythic.lib.util;

import io.lumine.mythic.lib.MythicLib;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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

    public static void runAsync(Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(MythicLib.plugin);
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        if (Bukkit.isPrimaryThread())
            return CompletableFuture.completedFuture(supplier.get());
        CompletableFuture<U> future = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    future.complete(supplier.get());
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        }.runTaskAsynchronously(MythicLib.plugin);
        return future;
    }

    public static net.md_5.bungee.api.ChatColor toBungee(ChatColor color) {
        return net.md_5.bungee.api.ChatColor.of(color.name());
    }
}
