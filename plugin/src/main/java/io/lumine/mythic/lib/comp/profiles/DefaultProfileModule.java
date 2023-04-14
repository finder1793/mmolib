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
public class DefaultProfileModule extends ProfileModule {

    @Override
    public boolean canLoadPlayerData() {
        return true;
    }

    @Override
    public UUID getUUID(Player player) {
        return player.getUniqueId();
    }



}