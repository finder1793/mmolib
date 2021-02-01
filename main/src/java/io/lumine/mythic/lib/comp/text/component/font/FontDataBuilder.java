package io.lumine.mythic.lib.comp.text.component.font;

import java.util.List;
import java.util.regex.Pattern;

public class FontDataBuilder extends ComponentDataBuilder {
    public FontDataBuilder(Pattern pattern, String format) {
        super(pattern, format);
    }

    @Override
    public List<ComponentData> build() {
        for (ComponentData componentData : this.getData()) {
            if (componentData.getKey() != null)
                componentData.setKey(formatFont(componentData.getKey()));
        }
        return this.getData();
    }

    private String formatFont(String font) {
        // Checks if the font id only has one colon.
        if (font.contains(":"))
            return font;
        // Adds default minecraft key.
        return "minecraft:" + font;
    }

}
