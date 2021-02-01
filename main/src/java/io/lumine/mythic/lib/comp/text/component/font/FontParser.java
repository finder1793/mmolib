package io.lumine.mythic.lib.comp.text.component.font;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Allows for 1.16 fonts to be converted from strings
 * inside components into completed components.
 *
 * @author Ehh
 */
public class FontParser extends ComponentParser {

    private static final Pattern PATTERN = Pattern.compile("(?i)<(?:f|font)=([:_.\\-\\w]+)>");

    @Override
    public BaseComponent parse(BaseComponent base) {
        // Creates a duplicate of the original component for editing.
        base = base.duplicate();

        // If no extra is specified, a blank line will be constructed.
        if (base.getExtra() == null)
            return base;

        StringBuilder format = new StringBuilder();
        for (BaseComponent component : base.getExtra()) {
            // Will not proceed if component is not Text.
            if (!(component instanceof TextComponent) || ((TextComponent) component).getText().equals("")) {
                continue;
            }

            // Converts to legacy text (String) and will later be converted back.
            format.append(component.toLegacyText());
        }
        if (format.length() < 1)
            return base;

        // Assigns strings to their respective fonts.
        List<ComponentData> builder = new FontDataBuilder(PATTERN, format.toString()).build();

        if (!builder.isEmpty()) {

            // Clears the extra and sets the initial component.
            base.setExtra(new ArrayList<>());
            BaseComponent parent = base;

            int i = 0;
            for (ComponentData data : builder) {
                for (BaseComponent component : TextComponent.fromLegacyText(data.getText())) {
                    // This condition determines if the component is the FIRST parent or not.
                    if (i == 0) {
                        base = component;
                    } else {
                        parent.addExtra(component);
                    }

                    if (!component.isItalic())
                        component.setItalic(false);
                    // Checks that a font if a font is not specified before applying one.
                    // Prevents items that have no font and are being generated with
                    // this new system to still stack accordingly.
                    if (data.getKey() != null) {
                        component.setFont(data.getKey());
                    }
                    // Sets the parent of the next component in the loop.
                    parent = component;
                    i++;
                }

            }
        }
        // Returns the base element that was duplicated from the initial argument.
        return base;
    }
}
