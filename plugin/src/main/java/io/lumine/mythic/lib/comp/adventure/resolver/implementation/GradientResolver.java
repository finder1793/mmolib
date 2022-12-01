package io.lumine.mythic.lib.comp.adventure.resolver.implementation;

import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgumentQueue;
import io.lumine.mythic.lib.comp.adventure.gradient.GradientBuilder;
import io.lumine.mythic.lib.comp.adventure.gradient.Interpolator;
import io.lumine.mythic.lib.comp.adventure.resolver.ContextTagResolver;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class GradientResolver implements ContextTagResolver {

    @Override
    public @Nullable String resolve(@NotNull String src, @NotNull AdventureArgumentQueue argsQueue, @NotNull String context) {
        if (!argsQueue.hasNext())
            return GradientBuilder.rgbGradient(context, Color.WHITE, Color.BLACK, Interpolator.LINEAR);
        List<String> args = new ArrayList<>();
        while (argsQueue.hasNext())
            args.add(argsQueue.pop().value());
        if (args.size() > 2)
            return GradientBuilder.multiRgbGradient(context, args.stream().map(this::color).toArray(Color[]::new), null, Interpolator.LINEAR);
        final Color c1 = color(args.get(0));
        if (args.size() == 1)
            return GradientBuilder.rgbGradient(context, c1, Color.BLACK, Interpolator.LINEAR);
        return GradientBuilder.rgbGradient(context, c1, color(args.get(1)), Interpolator.LINEAR);
    }

    private Color color(String raw) {
        try {
            ChatColor chatColor = ChatColor.of(raw);
            return chatColor.getColor();
        } catch (Exception e) {
            return Color.WHITE;
        }
    }

}
