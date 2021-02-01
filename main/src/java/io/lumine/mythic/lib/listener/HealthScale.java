package io.lumine.mythic.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HealthScale implements Listener {
    private final double scale;

    public HealthScale(double scale) {
        this.scale = scale;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setHealthScaled(true);
        player.setHealthScale(scale);
    }
}
