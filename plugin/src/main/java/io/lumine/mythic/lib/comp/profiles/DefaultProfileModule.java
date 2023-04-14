package io.lumine.mythic.lib.comp.profiles;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This is the default implementation of the profile module.
 * Every player only has one profile, it's the default one
 * and the corresponding UUID is just the player's UUID.
 */
public class DefaultProfileModule extends ProfileModule {

    @Override
    public boolean canLoadPlayerData() {
        return true;
    }

    @Override
    public UUID getCurrentId(Player player) {
        return player.getUniqueId();
    }
}