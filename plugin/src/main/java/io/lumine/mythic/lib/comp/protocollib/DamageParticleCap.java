package io.lumine.mythic.lib.comp.protocollib;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DamageParticleCap {
    // im typing this in discord so i dont think this is 100% correct

    private final Map<UUID, Integer> particles = new HashMap<>();
    private final int LIMIT; // Particles sent per tick

    public DamageParticleCap(int maxPerTick) {

        LIMIT = maxPerTick;

        // particle listener
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(MythicLib.plugin) {
            @Override
            public void onPacketSending(PacketEvent event) {
                onSend(event);
            }
        });
    }

    private void onSend(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final Player player = event.getPlayer();
        if (packet.getNewParticles().read(0).getParticle() != Particle.DAMAGE_INDICATOR) // I think that's what the particle is? if this doesn't work, try printing out the particle
            return;

        final int originalAmount = packet.getIntegers().read(0);
        final int storedAmount = particles.getOrDefault(player.getUniqueId(), 0); // particles already sent
        if (storedAmount >= LIMIT)
            return;

        final int amountLeft = LIMIT - storedAmount; // amount of particles left that can be sent this tick
        final int amount = Math.min(amountLeft, packet.getIntegers().read(0)); // amount of particles to send, limiting it to amountLeft
        if (amount <= 0) { // no particles
            event.setCancelled(true); // no point in sending a particle packet if there are none
            return;
        }

        Bukkit.getScheduler().runTask(MythicLib.plugin, () -> {
            final int current = particles.getOrDefault(player.getUniqueId(), 0);
            particles.put(player.getUniqueId(), current - amount);
        }); // put the counter down next tick

        if (amount == originalAmount)
            return; // nothing to change

        event.getPacket().getIntegers().write(0, amount); // set the new particle amount
    }
}
