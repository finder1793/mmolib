package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.element.Element;
import io.lumine.mythic.lib.util.ConfigFile;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class ElementManager {
    private final Map<String, Element> mapped = new HashMap<>();

    public void register(Element element) {
        Validate.isTrue(!mapped.containsKey(element.getId()), "An element already exists with the ID '" + element.getId() + "'");

        mapped.put(element.getId(), element);
    }

    public void reload(boolean clearBefore) {
        if (clearBefore)
            mapped.clear();
        else
            UtilityMethods.loadDefaultFile("", "elements.yml");

        FileConfiguration config = new ConfigFile("elements").getConfig();
        for (String key : config.getKeys(false))
            try {
                register(new Element(config.getConfigurationSection(key)));
            } catch (RuntimeException exception) {
                MythicLib.plugin.getLogger().log(Level.WARNING, "Could not load element '" + key + "': " + exception.getMessage());
            }
    }

    @NotNull
    public Element get(String id) {
        return Objects.requireNonNull(mapped.get(id), "No element found with ID '" + id + "'");
    }

    @Nullable
    public Element getOrNull(String id) {
        return mapped.get(id);
    }

    public Collection<Element> getAll() {
        return mapped.values();
    }
}
