package io.lumine.mythic.lib.player.modifier;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.Closeable;
import java.io.IOException;

public class TemporaryModifier implements Closeable {
    private final PlayerModifier modifier;
    private final BukkitRunnable closeTask;

    public TemporaryModifier(PlayerModifier modifier, long time) {
        this.modifier = modifier;

        closeTask = new BukkitRunnable() {
            @Override
            public void run() {
                unre
            }
        }.runTaskLater(MythicLib.plugin, time);

        modifier.register();
    }

    public void register(MMOPlayerData player) {
        modifier.register(player);

    }

    @Override
    public void close() throws IOException {
        closeTask.cancel();
    }
}
