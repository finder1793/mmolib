package io.lumine.mythic.lib.comp.profiles;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * MMOProfiles extends this module to support MythicLib.
 * <p>
 * Every profile owned by a player is associated to an UUID, it doesn't
 * have to have anything in common with the original player's UUID.
 */
public interface ProfileModule {

    /**
     * In practice, this option is also used when reloading the server.
     * Also, it's most likely set to false for PROFILE plugins, and set
     * to true when NOT using any profile plugin.
     *
     * @return Should MythicLib load player data when a player logs in.
     */
    public boolean loadsDataOnLogin();

    /**
     * Should throw an exception if the player is not online or
     * if he has not chosen a profile yet.
     *
     * @param playerId UUID of online player
     * @return The player's current profile ID of an online player.
     */
    @NotNull
    public UUID getCurrentId(UUID playerId);
}
