package io.lumine.mythic.lib.util.formula.process;

import io.lumine.mythic.lib.util.annotation.NotUsed;
import org.jetbrains.annotations.NotNull;

@NotUsed
@Deprecated
@FunctionalInterface
public interface PlaceholderProcessor {
    @NotNull
    public void processPlaceholder(@NotNull String placeholder);
}
