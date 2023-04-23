package io.lumine.mythic.lib.comp.profiles;

import java.util.UUID;

/**
 * This is the default implementation of the profile module.
 * Every player only has one profile, it's the default one
 * and the corresponding UUID is just the player's UUID.
 */
public class DefaultProfileModule implements ProfileModule {

    @Override
    public boolean loadsDataOnLogin() {
        return true;
    }

    @Override
    public boolean loadsDataOnStartup() {
        return true;
    }

    @Override
    public UUID getCurrentId(UUID uuid) {
        return uuid;
    }
}