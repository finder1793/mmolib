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
     * @return Should MythicLib load player data when a player logs in.
     */
    public boolean loadsDataOnLogin();

    /**
     * This option is very useful when using the /reload command
     * while players are still online when the server restarts.
     *
     * @return Should MythicLib load player data when the server starts up.
     */
    public boolean loadsDataOnStartup();

    /**
     * Should throw an exception if the player is not online.
     *
     * @param playerId UUID of online player
     * @return The player's current profile ID of an online player.
     */
    @NotNull
    public UUID getCurrentId(UUID playerId);
}
