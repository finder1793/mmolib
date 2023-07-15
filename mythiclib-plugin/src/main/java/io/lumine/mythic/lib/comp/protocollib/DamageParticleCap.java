package io.lumine.mythic.lib.comp.protocollib;

import com.comphenix.protocol.PacketType;
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
    private final Map<UUID, Integer> particles = new HashMap<>();

    /**
     * Maximum amount of particles sent per tick
     */
    private final int tickLimit;

    public DamageParticleCap(int tickLimit) {
        this.tickLimit = tickLimit;

        // Particle listener
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(MythicLib.plugin, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                onSend(event);
            }
        });
    }

    private void onSend(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final Player player = event.getPlayer();
        if (packet.getNewParticles().read(0).getParticle() != Particle.DAMAGE_INDICATOR)
            return;

        final int originalAmount = packet.getIntegers().read(0);
        final int storedAmount = particles.getOrDefault(player.getUniqueId(), 0); // Particles already sent
        if (storedAmount >= tickLimit)
            return;

        final int amountLeft = tickLimit - storedAmount; // Amount of particles left that can be sent this tick
        final int amount = Math.min(amountLeft, packet.getIntegers().read(0)); // Amount of particles to send, limiting it to amountLeft

        // No point in sending packet if 0 particles
        if (amount <= 0) {
            event.setCancelled(true);
            return;
        }

        // Update counter and put it down next tick
        particles.put(player.getUniqueId(), storedAmount + amount);
        Bukkit.getScheduler().runTask(MythicLib.plugin, () -> {
            final int current = particles.getOrDefault(player.getUniqueId(), 0);
            final int safeNew = Math.max(0, Math.min(tickLimit, current - amount));
            particles.put(player.getUniqueId(), safeNew);
        });

        // Nothing to change
        if (amount == originalAmount)
            return;

        event.getPacket().getIntegers().write(0, amount); // Set the new particle amount
    }
}
