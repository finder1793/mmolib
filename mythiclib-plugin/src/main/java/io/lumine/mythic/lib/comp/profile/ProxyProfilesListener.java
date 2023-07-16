package io.lumine.mythic.lib.comp.profile;

import fr.phoenixdevt.profiles.event.PlayerIdDispatchEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ProxyProfilesListener implements Listener {

    @EventHandler
    public void dispatchId(PlayerIdDispatchEvent event) {
        MMOPlayerData.setup(event.getPlayer()).setUniqueId(event.getInitialId());
    }
}
