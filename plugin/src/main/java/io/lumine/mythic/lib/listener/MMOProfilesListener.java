package io.lumine.mythic.lib.listener;

import fr.phoenix.mmoprofiles.API.events.PlayerChooseProfileEvent;
import fr.phoenix.mmoprofiles.API.events.PlayerRemoveProfileEvent;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MMOProfilesListener implements Listener {

    @EventHandler
    public void onProfileChoose(PlayerChooseProfileEvent event) {
        Player player = event.getPlayer();
        MMOPlayerData data = MMOPlayerData.setup(player, event.getProfileUUID());

        // Run stat updates on login
        MythicLib.plugin.getStats().runUpdates(data.getStatMap());
    }

    @EventHandler
    public void onProfileRemove(PlayerRemoveProfileEvent event) {

    }
}
