package io.lumine.mythic.lib.comp.profiles;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * MMOProfiles extends this module to support MythicLib.
 * <p>
 * Every profile owned by a player is associated to an UUID, it doesn't
 * have to have anything in common with the original player's UUID.
 */
public abstract class ProfileModule {

    public abstract boolean loadsDataOnLogin();

    public abstract UUID getCurrentId(Player player);

    @NotNull
    public UUID getCurrentId(OfflinePlayer offlinePlayer) {
        return offlinePlayer.isOnline() ? getCurrentId(offlinePlayer.getPlayer()) : offlinePlayer.getUniqueId();
    }

    public void loadProfileData(Player player, UUID uuid) {
        MMOPlayerData data = MMOPlayerData.setup(player, uuid);
        // Run stat updates on login
        MythicLib.plugin.getStats().runUpdates(data.getStatMap());
    }
}
