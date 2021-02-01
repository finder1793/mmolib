package io.lumine.mythic.lib.api.player;

import io.lumine.mythic.lib.api.stat.StatMap;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MMOPlayerData {
    private Player player;
    private long lastLogin;

    /*
     * stat data saved till next server startup
     */
    private final UUID uuid;
    private final StatMap stats = new StatMap(this);
    private final Map<MitigationType, Long> cooldowns = new HashMap<>();

    private static final Map<UUID, MMOPlayerData> data = new HashMap<>();

    public MMOPlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    @Deprecated
    public MMOPlayerData(Player player, UUID uuid) {
        this.player = player;
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    /***
     * @return The player's StatMap which can be used by any other plugins to
     *         apply stat modifiers to ANY MMOItems/MMOCore/external stats,
     *         calculate stat values, etc.
     */
    public StatMap getStatMap() {
        return stats;
    }

    /***
     * @return The last time the player logged in, timestamp in milliseconds.
     */
    @SuppressWarnings("unused")
    public long getLastLogin() {
        return lastLogin;
    }

    /***
     * @return If the player is currently online. This method simply checks if
     *         the cached Player instance is null because MMOLib uncaches it
     *         when the player leaves for memory purposes.
     */
    public boolean isOnline() {
        return player != null;
    }

    /***
     * @return Returns the corresponding Player instance, or throws an IAE if
     *         the player is currently not online. Make sure he is online before
     *         calling this method
     */
    public Player getPlayer() {
        Validate.notNull(player, "Player must be online");
        return player;
    }

    /***
     * Caches a new Player instance and refreshes the lastLogin value
     *
     * @param player Player instance to cache
     */
    public void setPlayer(Player player) {
        this.player = player;
        this.lastLogin = System.currentTimeMillis();
    }

    /***
     * Used when mitigation occurs to apply cooldown.
     *
     * @param cd    The type of mitigation
     * @param value Mitigation cooldown in seconds
     */
    public void applyCooldown(MitigationType cd, double value) {
        cooldowns.put(cd, (long) (System.currentTimeMillis() + value * 1000));
    }

    /***
     * @param  cd The type of mitigation involved
     * @return    If the mecanic is currently on cooldown for the player
     */
    public boolean isMitigationReady(MitigationType cd) {
        return !cooldowns.containsKey(cd) || cooldowns.get(cd) <= System.currentTimeMillis();
    }

    /***
     * Called everytime a player enters the server. Either initializes the
     * MMOPlayerData instance, or caches the Player instance + refreshes the
     * lastLogin value
     *
     * @param player The player to setup
     */
    public static void setup(Player player) {
        if (!data.containsKey(player.getUniqueId()))
            data.put(player.getUniqueId(), new MMOPlayerData(player));
        else
            data.get(player.getUniqueId()).setPlayer(player);
    }

    /***
     * This essentially checks if a player logged in since the last time the
     * server started/was reloaded.
     *
     * @param  uuid The player UUID to check
     * @return      If the MMOPlayerData instance is loaded for a specific
     *              player
     */
    public static boolean isLoaded(UUID uuid) {
        return data.containsKey(uuid);
    }

    public static MMOPlayerData get(OfflinePlayer player) {
        return data.get(player.getUniqueId());
    }

    public static MMOPlayerData get(UUID uuid) {
        return data.get(uuid);
    }

    /***
     * @return Currently loaded MMOPlayerData instances. This can be used to
     *         apply things like resource regeneration or other runnable based
     *         tasks instead of looping through online players and having to
     *         resort to a map-lookup-based get(Player) call
     */
    public static Collection<MMOPlayerData> getLoaded() {
        return data.values();
    }
}

