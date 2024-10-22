package io.lumine.mythic.lib.player.cooldown;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CooldownMap {
    private final Map<NamespacedKey, CooldownInfo> map = new HashMap<>();

    /**
     * Sets current cooldown of given reference. If the player
     * already has a cooldown registered to the provided reference,
     * the maximum of the two values is used.
     *
     * @param provider Skill or action provider
     * @return The current player's cooldown info
     */
    @NotNull
    public CooldownInfo applyCooldown(@NotNull CooldownProvider provider) {
        return applyCooldown(provider.getCooldownKey(), ((double) provider.getCooldown()) / 1000d);
    }

    /**
     * Sets current cooldown of given reference. If the player
     * already has a cooldown registered to the provided reference,
     * the maximum of the two values is used.
     *
     * @param ref      The skill or action reference, must be completely unique
     * @param cooldown Initial skill or action cooldown
     * @return The current player's cooldown info
     */
    @NotNull
    public CooldownInfo applyCooldown(@NotNull CooldownReference ref, double cooldown) {
        return applyCooldown(ref.getCooldownKey(), cooldown);
    }

    /**
     * Sets current cooldown of given reference. If the player
     * already has a cooldown registered to the provided reference,
     * the maximum of the two values is used.
     *
     * @param ref      The skill or action reference, must be completely unique
     * @param cooldown Initial skill or action cooldown
     * @return The current player's cooldown info
     */
    @NotNull
    public CooldownInfo applyCooldown(@NotNull NamespacedKey ref, double cooldown) {
        return map.compute(ref, (ignored, current) -> {
            if (current != null && current.getRemaining() >= cooldown * 1000) return current;
            return new CooldownInfo(cooldown);
        });
    }

    /**
     * @return Cooldown info for provided cooldown reference
     */
    @Nullable
    public CooldownInfo getInfo(@NotNull CooldownReference ref) {
        return getInfo(ref.getCooldownKey());
    }

    /**
     * @return Cooldown info for provided cooldown reference
     */
    @Nullable
    public CooldownInfo getInfo(@NotNull NamespacedKey path) {
        return map.get(path);
    }

    /**
     * @param ref The skill or action reference, must be completely unique
     * @return Retrieves the remaining cooldown in seconds
     */
    public double getCooldown(@NotNull CooldownReference ref) {
        return getCooldown(ref.getCooldownKey());
    }

    /**
     * @param key The skill or action reference, must be completely unique
     * @return Retrieves the remaining cooldown in seconds
     */
    public double getCooldown(@NotNull NamespacedKey key) {
        final @Nullable CooldownInfo info = map.get(key);
        return info == null ? 0 : (double) info.getRemaining() / 1000;
    }

    /**
     * @param ref The skill or action reference, must be completely unique
     * @return The last time some action or skill was performed
     */
    public long getLast(@NotNull CooldownReference ref) {
        return getLast(ref.getCooldownKey());
    }

    /**
     * @param key The skill or action reference, must be completely unique
     * @return The last time some action or skill was performed
     */
    public long getLast(@NotNull NamespacedKey key) {
        final @Nullable CooldownInfo info = map.get(key);
        return info == null ? 0 : info.getLast();
    }

    /**
     * @param ref The skill or action reference, must be completely unique
     * @return If the mechanic can be used by the player
     */
    public boolean isOnCooldown(@NotNull CooldownReference ref) {
        return isOnCooldown(ref.getCooldownKey());
    }

    /**
     * @param key The skill or action reference, must be completely unique
     * @return If the mechanic can be used by the player
     */
    public boolean isOnCooldown(@NotNull NamespacedKey key) {
        final @Nullable CooldownInfo found = map.get(key);
        return found != null && !found.hasEnded();
    }

    /**
     * Entirely resets a cooldown for given action.
     *
     * @param ref The skill or action
     */
    @Nullable
    public CooldownInfo resetCooldown(@NotNull CooldownReference ref) {
        return resetCooldown(ref.getCooldownKey());
    }

    /**
     * Entirely resets a cooldown for given path.
     *
     * @param key The skill or action path, must be completely unique
     */
    @Nullable
    public CooldownInfo resetCooldown(@NotNull NamespacedKey key) {
        return map.remove(key);
    }

    //region deprecated

    @Deprecated
    public CooldownInfo applyCooldown(String path, double cooldown) {
        return applyCooldown(new NamespacedKey("legacy", path), cooldown);
    }

    @Deprecated
    public CooldownInfo getInfo(String path) {
        return getInfo(new NamespacedKey("legacy", path));
    }

    @Deprecated
    public double getCooldown(String path) {
        return getCooldown(legacyKey(path));
    }

    @Deprecated
    public boolean isOnCooldown(String path) {
        return isOnCooldown(legacyKey(path));
    }

    @Deprecated
    public void resetCooldown(String path) {
        resetCooldown(legacyKey(path));
    }

    @Deprecated
    public static NamespacedKey legacyKey(String path) {
        return new NamespacedKey("legacy", path);
    }

    //endregion
}
