package io.lumine.mythic.lib.comp;

import io.lumine.mythic.lib.api.EntityHandler;
import org.bukkit.entity.Entity;

public class MyPetEntityHandler implements EntityHandler {

    @Override
    public boolean isInvulnerable(Entity entity) {
        return entity instanceof MyPetBukkitEntity;
    }
}
