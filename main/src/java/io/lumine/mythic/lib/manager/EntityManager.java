package io.lumine.mythic.lib.manager;

import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    public final List<EntityHandler> handlers = new ArrayList<>();

    public void registerHandler(EntityHandler handler) {
        handlers.add(handler);
    }

    /*
     * determines if an entity is from another plugin and therefore cannot be
     * target of skill or attack
     */
    public boolean findCustom(Entity entity) {

        for (EntityHandler handler : handlers)
            if (handler.isInvulnerable(entity))
                return true;

        return false;
    }
}
