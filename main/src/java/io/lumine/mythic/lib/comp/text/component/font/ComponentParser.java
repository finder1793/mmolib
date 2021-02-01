package io.lumine.mythic.lib.comp.text.component.font;

import net.md_5.bungee.api.chat.BaseComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface used for any component type that needs a
 * string -> component element parser.
 *
 * Ex. <font=uniform> -> Json: {"font":"minecraft:uniform"}
 *
 * @author Ehh
 */

public abstract class ComponentParser {

    /**
     * Used for singular components.
     * Ex. Display Name
     *
     * @param base Component to parse.
     *
     * @return Parsed Component
     */
    public abstract BaseComponent parse(BaseComponent base);

    /**
     * Used for lists of components
     * Ex. Lore
     *
     * @param base Component List to parse.
     *
     * @return Parsed Component List
     */
    public List<BaseComponent> parse(List<BaseComponent> base) {
        List<BaseComponent> components = new ArrayList<>();

        for (BaseComponent baseComponent : base) {
            components.add(parse(baseComponent));
        }

        return components;
    }
}
