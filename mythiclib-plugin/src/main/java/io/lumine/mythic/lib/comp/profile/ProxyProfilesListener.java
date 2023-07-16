package io.lumine.mythic.lib.comp.profile;

import fr.phoenixdevt.profiles.event.PlayerIdDispatchEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.apache.commons.lang.Validate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class ProxyProfilesListener implements Listener {

    @EventHandler
    public void dispatchId(PlayerIdDispatchEvent event) {
        final MMOPlayerData playerData = MMOPlayerData.setup(event.getPlayer());

        // Basic verification
        Validate.isTrue(event.getPlayer().getUniqueId().equals(Objects.requireNonNullElse(event.getFakeId(), event.getInitialId())), "Could not verify player fake UUID");

        // Update UUIDs saved by MythicLib
        playerData.setUniqueId(event.getInitialId());
        playerData.setProfileId(event.getFakeId());
    }
}
