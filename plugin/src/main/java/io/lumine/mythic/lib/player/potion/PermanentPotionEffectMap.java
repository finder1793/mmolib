package io.lumine.mythic.lib.player.potion;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.player.modifier.Closeable;
import io.lumine.mythic.lib.player.modifier.ModifierMap;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PermanentPotionEffectMap implements ModifierMap<PermanentPotionEffect> {
    private final MMOPlayerData playerData;
    private final Map<UUID, PermanentPotionEffect> effects = new HashMap<>();

    private final Map<PotionEffectType, Integer> maxLevels = new HashMap<>();

    public PermanentPotionEffectMap(MMOPlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public MMOPlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public Collection<PermanentPotionEffect> getModifiers() {
        return effects.values();
    }

    @Override
    public void addModifier(PermanentPotionEffect effect) {
        effects.put(effect.getUniqueId(), effect);

        // Update cached map
        PotionEffectType key = effect.getEffect();
        maxLevels.put(key, Math.max(maxLevels.getOrDefault(key, -1), effect.getAmplifier()));
    }

    @Override
    public void removeModifier(UUID uniqueId) {
        PermanentPotionEffect effect = effects.remove(uniqueId);

        // Recalculate max
        if (effect != null) {
            if (effect instanceof Closeable)
                ((Closeable) effect).close();
            updateHighestLevel(effect.getEffect());
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
        for (Iterator<PermanentPotionEffect> iterator = effects.values().iterator(); iterator.hasNext(); ) {
            PermanentPotionEffect entry = iterator.next();
            if (entry.getKey().equals(key)) {
                iterator.remove();
                if (entry instanceof Closeable)
                    ((Closeable) entry).close();
                updateHighestLevel(entry.getEffect());
            }
        }
    }

    public void applyPermanentEffects() {
        Validate.isTrue(playerData.isOnline(), "Player is offline");

        Player player = playerData.getPlayer();
        maxLevels.forEach((type, level) -> {
            int currentAmplifier = player.hasPotionEffect(type) ? player.getPotionEffect(type).getAmplifier() : -1;
            if (level >= currentAmplifier)
                player.addPotionEffect(new PotionEffect(type, getEffectDuration(type), level, false, false));
        });
    }

    /**
     * Takes all the modifiers into account to calculate the highest
     * potion effect level i.e the level the player should have.
     *
     * @param type Potion effect type
     * @return Highest level of potion effect or -1 if none
     */
    public int getHighestLevel(PotionEffectType type) {
        int amplifier = -1;

        for (PermanentPotionEffect perm : effects.values())
            if (perm.getEffect() == type)
                amplifier = Math.max(amplifier, perm.getAmplifier());

        return amplifier;
    }

    private void updateHighestLevel(PotionEffectType type) {
        int max = getHighestLevel(type);
        if (max == -1)
            maxLevels.remove(type);
        else
            maxLevels.put(type, max);
    }

    /**
     * The last 5 seconds of nausea are useless, night vision flashes in the
     * last 10 seconds, blindness takes a few seconds to decay as well, and
     * there can be small server lags. It's best to apply a specific duration
     * for every type of permanent effect.
     *
     * @param type Potion effect type
     * @return The duration that MythicLib should be using to give player
     *         "permanent" potion effects, depending on the potion effect type
     */
    private int getEffectDuration(PotionEffectType type) {
        return type.equals(PotionEffectType.NIGHT_VISION) || type.equals(PotionEffectType.CONFUSION) ? 260 : type.equals(PotionEffectType.BLINDNESS) ? 140 : 80;
    }
}
