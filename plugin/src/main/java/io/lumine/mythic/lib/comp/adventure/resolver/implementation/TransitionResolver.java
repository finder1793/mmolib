package io.lumine.mythic.lib.comp.adventure.resolver.implementation;

import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgumentQueue;
import io.lumine.mythic.lib.comp.adventure.resolver.ContextTagResolver;
import io.lumine.mythic.lib.util.AdventureUtils;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * mythiclib
 * 05/12/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class TransitionResolver implements ContextTagResolver {
    @Override
    public @Nullable String resolve(@NotNull String src, @NotNull AdventureArgumentQueue argumentQueue, @NotNull String context) {
        List<String> args = new ArrayList<>();
        while (argumentQueue.hasNext())
            args.add(argumentQueue.pop().value());
        if (args.size() < 3)
            return null;
        double phase = getPhase(args);
        if (phase < 0 || phase > 1)
            return null;
        List<Color> colors = args.stream()
                .map(AdventureUtils::color)
                .collect(Collectors.toList());
        if (colors.size() < 2)
            return null;
        return ChatColor.of(transition(phase, colors)) + context;
    }

    private Color transition(double phase, List<Color> colors) {
        int index = (int) (phase * (colors.size() - 1));
        double localPhase = phase * (colors.size() - 1) - index;
        if (phase == 0)
            return colors.get(0);
        if (phase == 1)
            return colors.get(colors.size() - 1);
        Color color1 = colors.get(index);
        Color color2 = colors.get(index + 1);
        return new Color(
                (int) (color1.getRed() * (1 - localPhase) + color2.getRed() * localPhase),
                (int) (color1.getGreen() * (1 - localPhase) + color2.getGreen() * localPhase),
                (int) (color1.getBlue() * (1 - localPhase) + color2.getBlue() * localPhase),
                (int) (color1.getAlpha() * (1 - localPhase) + color2.getAlpha() * localPhase)
        );
    }

    private double getPhase(List<String> args) {
        String lastArg = args.get(args.size() - 1);
        try {
            double phase = Double.parseDouble(lastArg);
            args.remove(args.size() - 1);
            return phase;
        } catch (NumberFormatException e) {
            return 1d;
        }
    }
}
