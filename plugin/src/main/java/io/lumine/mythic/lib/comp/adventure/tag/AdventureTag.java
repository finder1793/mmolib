package io.lumine.mythic.lib.comp.adventure.tag;

import io.lumine.mythic.lib.comp.adventure.resolver.AdventureTagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public abstract class AdventureTag {

    private final String name;
    private final List<String> aliases = new ArrayList<>();
    private final AdventureTagResolver resolver;

    public AdventureTag(@NotNull String name, @NotNull AdventureTagResolver resolver, @NotNull String... aliases) {
        this.name = name;
        this.resolver = resolver;
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public AdventureTagResolver resolver() {
        return resolver;
    }

    public String name() {
        return name;
    }

    public List<String> aliases() {
        return aliases;
    }
}