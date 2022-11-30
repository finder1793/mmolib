package io.lumine.mythic.lib.comp.adventure.resolver;

import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgumentQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
@FunctionalInterface
public interface ContextTagResolver extends AdventureTagResolver {

    /**
     * Resolve a tag.
     *
     * @param src           the source of the tag.
     * @param argumentQueue the argument queue.
     * @param context       the context of the tag.
     * @return the resolved tag
     */
    @Nullable String resolve(@NotNull String src, @NotNull AdventureArgumentQueue argumentQueue, @NotNull String context);

    @Override
    default @Nullable String resolve(@NotNull String src, @NotNull AdventureArgumentQueue argumentQueue) {
        return null;
    }
}
