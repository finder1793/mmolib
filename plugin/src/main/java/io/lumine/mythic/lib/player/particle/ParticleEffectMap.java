package io.lumine.mythic.lib.player.particle;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.player.modifier.Closeable;
import io.lumine.mythic.lib.player.modifier.ModifierMap;

import java.util.*;

public class ParticleEffectMap implements ModifierMap<ParticleEffect> {
    private final MMOPlayerData playerData;
    private final Map<UUID, ParticleEffect> effects = new HashMap<>();

    public ParticleEffectMap(MMOPlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public MMOPlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public Collection<ParticleEffect> getModifiers() {
        return effects.values();
    }

    @Override
    public void addModifier(ParticleEffect effect) {
        effects.put(effect.getUniqueId(), effect);

        // TODO update
    }

    @Override
    public void removeModifier(UUID uniqueId) {
        ParticleEffect effect = effects.remove(uniqueId);

        if (effect != null) {
            if (effect instanceof Closeable)
                effect.close();

            // TODO update
        }
    }

    /**
     * Iterates through registered modifiers and unregister
     * those with a specific modifier key.
     *
     * @param key Modifier key to unregister
     */
    @Override
    public void removeModifiers(String key) {
        boolean check = false;

        for (Iterator<ParticleEffect> iterator = effects.values().iterator(); iterator.hasNext(); ) {
            ParticleEffect entry = iterator.next();
            if (entry.getKey().equals(key)) {

                if (entry instanceof Closeable)
                    ((Closeable) entry).close();

                iterator.remove();
                check = true;
            }
        }

        if (check) {
            // TODO update
        }
    }
}
