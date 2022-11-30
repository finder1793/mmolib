package io.lumine.mythic.lib.comp.adventure.tag.implementation.decorations;

import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgumentQueue;
import io.lumine.mythic.lib.comp.adventure.resolver.AdventureTagResolver;
import io.lumine.mythic.lib.comp.adventure.tag.AdventureTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class BoldTag extends AdventureTag {

    public BoldTag() {
        super("bold", new BoldResolver(), true,"b");
    }

    public static class BoldResolver implements AdventureTagResolver {

        @Override
        public @Nullable String resolve(@NotNull String src, @NotNull AdventureArgumentQueue argumentQueue) {
            return "§l";
        }
    }
}
