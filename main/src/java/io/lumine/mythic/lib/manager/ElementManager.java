package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.element.Element;
import org.apache.commons.lang.Validate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ElementManager {
    private final Map<String, Element> mapped = new HashMap<>();

    public void register(Element element) {
        Validate.isTrue(!mapped.containsKey(element.getId()), "An element already exists with the ID '" + element.getId() + "'");

        mapped.put(element.getId(), element);
    }

    public Element get(String id) {
        return mapped.get(id);
    }

    public Collection<Element> getAll() {
        return mapped.values();
    }
}
