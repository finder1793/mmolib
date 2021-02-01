package io.lumine.mythic.lib.comp.text.component.font;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentDataBuilder {
    private final Pattern pattern;

    private final String format;

    public ComponentDataBuilder(Pattern pattern, String format) {
        this.pattern = pattern;
        this.format = format;
    }

    public List<ComponentData> build() {
        return getData();
    }

    public List<ComponentData> getData() {
        // Can't think of a better method of doing this
        // currently. Should be improved IMO.
        List<String> keys = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        // Regex does regex things :)
        Matcher match = pattern.matcher(format);
        while (match.find()) {
            keys.add(match.group(1));
            indices.add(match.start());
            indices.add(match.end());
        }

        List<ComponentData> componentData = new ArrayList<>();

        if (!indices.isEmpty()) {
            // Checks if there is text without a font before
            // the first font occurrence.
            if (indices.get(0) != 0)
                componentData.add(new ComponentData(null, format.substring(0, indices.get(0))));

            // Removes first index as it is redundant.
            indices.remove(0);
            // Gets absolute last index
            indices.add(format.length());

            int i = 0;
            for (String key : keys) {
                componentData.add(new ComponentData(key,
                        format.substring(indices.get(i), indices.get(i + 1)
                        )));
                i += 2;
            }

        }
        return componentData;
    }
}
