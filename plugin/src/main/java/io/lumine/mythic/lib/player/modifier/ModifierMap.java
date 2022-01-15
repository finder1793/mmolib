package io.lumine.mythic.lib.player.modifier;

import io.lumine.mythic.lib.api.player.MMOPlayerData;

import java.util.Collection;
import java.util.UUID;

public interface ModifierMap<T extends PlayerModifier> {
    MMOPlayerData getPlayerData();

    Collection<T> getModifiers();

    void addModifier(T modifier);

    void removeModifier(UUID uuid);

    void removeModifiers(String key);
}
