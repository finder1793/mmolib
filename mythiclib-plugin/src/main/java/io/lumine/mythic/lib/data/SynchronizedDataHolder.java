package io.lumine.mythic.lib.data;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * This class is implemented by player datas. A small boolean field
 * is absolutely necessary to keep track if player data has already
 * been loaded or not.
 * <p>
 * This class used to contain a reference to a MMOPlayerData instance.
 * Since it only needs the UUID used to save player data, the only
 * data it really needs is a profile ID.
 *
 * @author jules
 */
public abstract class SynchronizedDataHolder implements OfflineDataHolder {
    private final MMOPlayerData playerData;
    private final boolean profilePlugin;

    /**
     * This boolean dictates whether the player data was loaded
     * and if it can be saved again in the remote/local database.
     */
    private boolean sync;

    public SynchronizedDataHolder(@NotNull MMOPlayerData playerData) {
        this(false, playerData);
    }

    /**
     * @param profilePlugin If the plugin creating the player data is a profile plugin
     * @param playerData    Parent MythicLib player data
     */
    public SynchronizedDataHolder(boolean profilePlugin, @NotNull MMOPlayerData playerData) {
        this.profilePlugin = profilePlugin;
        this.playerData = playerData;
    }

    @NotNull
    public MMOPlayerData getMMOPlayerData() {
        return playerData;
    }

    @Override
    @NotNull
    public UUID getUniqueId() {
        return playerData.getUniqueId();
    }

    @NotNull
    public UUID getProfileId() {
        return playerData.getProfileId();
    }

    @NotNull
    public UUID getOfficialId() {
        return playerData.getOfficialId();
    }

    @NotNull
    public Player getPlayer() {
        return playerData.getPlayer();
    }

    /**
     * @return The UUID used to save player data inside of the database.
     */
    @NotNull
    public UUID getEffectiveId() {

        // No profiles => All IDs match
        if (MythicLib.plugin.getProfileMode() == null) return getUniqueId();

        // Profile plugin => take official ID (if proxy-based profiles are enabled), unique ID otherwise (legacy profiles)
        if (profilePlugin) return playerData.hasOfficialId() ? getOfficialId() : getUniqueId();

        // Otherwise, take profile ID if it exists
        return playerData.hasProfile() ? getProfileId() : getUniqueId();
    }

    @Deprecated
    public boolean shouldBeSaved() {
        return isSynchronized();
    }

    /**
     * @return If the synchronized data has already been loaded.
     */
    public boolean isSynchronized() {
        return sync;
    }

    public void markAsSynchronized() {
        Validate.isTrue(!sync, "Data holder already marked synchronized");
        sync = true;
    }
}
