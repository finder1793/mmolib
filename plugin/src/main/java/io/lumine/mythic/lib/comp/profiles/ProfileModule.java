package io.lumine.mythic.lib.comp.profiles;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This is the default implementation of the profile module.
 * MMOProfiles extends this module to make itself compatible with MythicLib.
 */
public abstract class ProfileModule {

    public abstract boolean canLoadPlayerData();

    public abstract UUID getUUID(Player player);

    public UUID getUUID(OfflinePlayer offlinePlayer) {
        if(offlinePlayer instanceof Player)
            return getUUID((Player) offlinePlayer);
        return offlinePlayer.getUniqueId();
    }

    public void loadProfileData(Player player, UUID uuid) {
        MMOPlayerData data = MMOPlayerData.setup(player, uuid);
        // Run stat updates on login
        MythicLib.plugin.getStats().runUpdates(data.getStatMap());
    }

}
