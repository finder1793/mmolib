package io.lumine.mythic.lib.comp.text.component.font;

import io.lumine.mythic.lib.MythicLib;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the builder of a finalized component.
 *
 * @author Ehh
 */
public class ComponentBuilder extends ComponentParser {
    // Set of what parsers are enabled and allowed to be used.
    private final Set<ComponentParser> enabledParsers = new LinkedHashSet<>();

    /**
     * Currently only contains font parsing, will be
     * updated when new components are added.
     */
    public ComponentBuilder() {
        // Added in 1.16
        if (MythicLib.plugin.getVersion().isStrictlyHigher(1, 15)) {
            enabledParsers.add(new FontParser());
        }
    }

    /**
     * Individually parses an sets each component according
     * to each enabled parser's result.
     */
    @Override
    public BaseComponent parse(BaseComponent base) {
        if (enabledParsers.isEmpty())
            return base;

        base = base.duplicate();
        for (ComponentParser parser : enabledParsers) {
            try {
                base = parser.parse(base);
            } catch (NullPointerException ignored) {}
        }
        return base;
    }
}
