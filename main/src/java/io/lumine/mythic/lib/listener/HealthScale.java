package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.Schedulers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HealthScale implements Listener {
    private final double scale;
    private final int delay;


    public HealthScale(double scale, int delay) {
        this.scale = scale;
        this.delay = delay;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Schedulers.bukkit().runTaskLater(MythicLib.plugin, () -> {
            player.setHealthScaled(true);
            player.setHealthScale(scale);
        }, delay);
    }
}
