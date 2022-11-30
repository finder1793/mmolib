package io.lumine.mythic.lib.comp.adventure.tag.implementation;

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
public class NewlineTag extends AdventureTag {

    public NewlineTag() {
        super("newline", new NewlineResolver(), true, "br");
    }

    public static class NewlineResolver implements AdventureTagResolver {
        @Override
        public @Nullable String resolve(@NotNull String src, @NotNull AdventureArgumentQueue argumentQueue) {
            return "\n";
        }
    }
}
