package io.lumine.mythic.lib.api.player;

import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.listener.PlayerListener;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MMOPlayerData {
    private Player player;

    /**
     * Last time the player either logged in or logged out.
     */
    private long lastLogActivity;

    private final UUID uuid;

    // Data saved till next server restart
    private final Map<CooldownType, Long> nextUse = new HashMap<>();
    private final StatMap stats = new StatMap(this);

    private static final Map<UUID, MMOPlayerData> data = new HashMap<>();

    private MMOPlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    /**
     * @return The player's StatMap which can be used by any other plugins to
     * apply stat modifiers to ANY MMOItems/MMOCore/external stats,
     * calculate stat values, etc.
     */
    public StatMap getStatMap() {
        return stats;
    }

    /**
     * @return The last time, in millis, the player logged in or out
     * @deprecated Use {@link #getLastLogActivity()} instead
     */
    @Deprecated
    public long getLastLogin() {
        return getLastLogActivity();
    }

    /**
     * @return The last time, in millis, the player logged in or out
     */
    public long getLastLogActivity() {
        return lastLogActivity;
    }

    /**
     * This method simply checks if the cached Player instance is null
     * because MMOLib uncaches it when the player leaves for memory purposes.
     *
     * @return If the player is currently online.
     */
    public boolean isOnline() {
        return player != null;
    }

    /**
     * Throws an IAE if the player is currently not online
     * OR if the Player instance was not cached in yet.
     * <p>
     * MythicLib updates the Player instance on event priority LOW
     * using {@link PlayerJoinEvent} here: {@link PlayerListener}
     *
     * @return Returns the corresponding Player instance.
     */
    public Player getPlayer() {
        Validate.notNull(player, "Player is offline");
        return player;
    }

    /**
     * Caches a new Player instance and refreshes the last log activity
     *
     * @param player Player instance to cache
     */
    public void updatePlayer(Player player) {
        this.player = player;
        this.lastLogActivity = System.currentTimeMillis();
    }

    /**
     * Used when damage mitigation or a crit occurs to apply cooldown
     *
     * @param cd    The type of mitigation
     * @param value Mitigation cooldown in seconds
     */
    public void applyCooldown(CooldownType cd, double value) {
        nextUse.put(cd, (long) (System.currentTimeMillis() + value * 1000));
    }

    /**
     * @param cd Cooldown type
     * @return If the mecanic is currently on cooldown for the player
     */
    public boolean isOnCooldown(CooldownType cd) {
        return nextUse.containsKey(cd) && nextUse.get(cd) > System.currentTimeMillis();
    }

    /**
     * Called everytime a player enters the server. If the
     * resource data is not initialized yet, initializes it.
     * <p>
     * This is called async using {@link AsyncPlayerPreLoginEvent} which does
     * not provide a Player instance, meaning the cached Player instance is NOT
     * loaded yet. It is only loaded when the player logs in using {@link PlayerJoinEvent}
     *
     * @param uuid Player id to be loaded
     */
    public static MMOPlayerData setup(UUID uuid) {
        if (!data.containsKey(uuid)) {
            MMOPlayerData playerData = new MMOPlayerData(uuid);
            data.put(uuid, playerData);
            return playerData;
        }

        return data.get(uuid);
    }

    /**
     * This essentially checks if a player logged in since the last time the
     * server started/was reloaded.
     *
     * @param uuid The player UUID to check
     * @return If the MMOPlayerData instance is loaded for a specific
     * player
     */
    public static boolean isLoaded(UUID uuid) {
        return data.containsKey(uuid);
    }

    @Contract("null -> null")
    @Nullable
    public static MMOPlayerData get(@Nullable OfflinePlayer player) {
        return player == null ? null : data.get(player.getUniqueId());
    }

    public static MMOPlayerData get(UUID uuid) {
        return data.get(uuid);
    }

    /**
     * @return Currently loaded MMOPlayerData instances. This can be used to
     * apply things like resource regeneration or other runnable based
     * tasks instead of looping through online players and having to
     * resort to a map-lookup-based get(Player) call
     */
    public static Collection<MMOPlayerData> getLoaded() {
        return data.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MMOPlayerData)) return false;

        MMOPlayerData that = (MMOPlayerData) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}

