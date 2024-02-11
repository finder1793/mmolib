package io.lumine.mythic.lib.util.network;

import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minecraft vanilla behaviour (glitched):
 * - distance < 3, successful melee hit, no interact packet but attack packet.
 * - distance 3 - 5, missed melee attack, no interact packet, no attack packet.
 * - distance > 5, not an attack, interact packet.
 * - at any distance, swing packet.
 * <p>
 * This class fixes when the entity is at distance <5 to send an interact
 * packet at this situation. The recreated behaviour is that interact events
 * are called EVERYTIME.
 *
 * @author Roch Blondiaux (Kiwix). 18/05/2023
 */
public class MythicPacketSniffer extends LightInjector {
    private static final double MAX_RANGE = 5;

    private static final Map<String, Map<String, Field>> FIELDS = new ConcurrentHashMap<>();

    /**
     * Initializes the injector and starts to listen to packets.
     * <p>
     * Note that, while it is possible to create more than one instance per plugin,
     * it's more efficient and recommended to just have only one.
     *
     * @param plugin The {@link Plugin} which is instantiating this injector.
     * @throws NullPointerException     If the provided {@code plugin} is {@code null}.
     * @throws IllegalStateException    When <b>not</b> called from the main thread.
     * @throws IllegalArgumentException If the provided {@code plugin} is not enabled.
     */
    public MythicPacketSniffer(@NotNull Plugin plugin) {
        super(plugin);
    }

    @Override
    protected @Nullable Object onPacketReceiveAsync(@Nullable Player sender, @NotNull Channel channel, @NotNull Object packet) {
        if (sender == null) return packet;
        final String packetName = packet.getClass().getSimpleName();

        if (packetName.equals("PacketPlayInArmAnimation")) {
            Object hand = getField(packet, "a");
            if (hand == null || !hand.toString().equals("MAIN_HAND")) return packet;

            // We need to run this synchronously because Bukkit sucks
            Bukkit.getScheduler().runTask(getPlugin(), () -> {
                final SeenEntity entity = getLineOfSight(sender);
                if (entity != null)
                    triggerEvent(sender);
            });
        }

        return packet;
    }

    @Override
    protected @Nullable Object onPacketSendAsync(@Nullable Player receiver, @NotNull Channel channel, @NotNull Object packet) {
        return packet;
    }

    /**
     * Approximate re-implementation of the line of sight of a
     * player, that is the entity that the player is looking at.
     *
     * @return The entity in line of sight as well as their distance
     * to the player's camera
     */
    @Nullable
    private SeenEntity getLineOfSight(Player player) {
        final RayTraceResult result = player.getWorld().rayTrace(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                MAX_RANGE,
                FluidCollisionMode.NEVER,
                true,
                0,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (result == null) return null;
        final Entity entity = result.getHitEntity();
        if (entity == null || entity instanceof Player && ((Player) entity).getGameMode().ordinal() == 3) return null;
        final double distance = result.getHitPosition().distance(player.getEyeLocation().toVector());
        return new SeenEntity(entity, distance);
    }

    private void triggerEvent(@NotNull Player player) {
        Bukkit.getPluginManager().callEvent(new PlayerInteractEvent(player,
                Action.LEFT_CLICK_AIR,
                player.getInventory().getItemInMainHand(),
                null,
                BlockFace.EAST,
                EquipmentSlot.HAND));
    }

    @Nullable
    private Object getField(Object object, String field) {
        return getField(object, object.getClass(), field);
    }

    @Nullable
    private Object getField(Object object, Class<?> c, String field) {
        if (FIELDS.containsKey(c.getCanonicalName())) {
            Map<String, Field> fs = FIELDS.get(c.getCanonicalName());
            if (fs.containsKey(field)) {
                try {
                    return fs.get(field).get(object);
                } catch (ReflectiveOperationException e) {
                    return null;
                }
            }
        }

        Class<?> current = c;
        Field f;
        while (true)
            try {
                f = current.getDeclaredField(field);
                break;
            } catch (ReflectiveOperationException e1) {
                current = current.getSuperclass();
                if (current != null) {
                    continue;
                }
                return null;
            }

        f.setAccessible(true);

        Map<String, Field> map;
        if (FIELDS.containsKey(c.getCanonicalName())) {
            map = FIELDS.get(c.getCanonicalName());
        } else {
            map = new ConcurrentHashMap<>();
            FIELDS.put(c.getCanonicalName(), map);
        }

        map.put(f.getName(), f);

        try {
            return f.get(object);
        } catch (ReflectiveOperationException e) {
            return null;
        }

    }

    private class SeenEntity {
        @NotNull
        final Entity entity;
        final double distance;

        private SeenEntity(@NotNull Entity entity, double distance) {
            this.entity = entity;
            this.distance = distance;
        }
    }
}
