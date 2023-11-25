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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fixes two things:
 * - Bukkit not sending LEFT CLICK interact events on successful/missed
 * melee hits (1.20.2+)
 * - Bukkit not sending LEFT CLICK interact events when the hit entity
 * is too close to be an AIR interact, too far to be a successful hit
 * (1.14-1.20.1)
 *
 * @author Roch Blondiaux (Kiwix). 18/05/2023
 */
public class MythicPacketSniffer extends LightInjector {
    private final double range;

    private static final Map<String, Map<String, Field>> fields = new ConcurrentHashMap<>();

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
    public MythicPacketSniffer(@NotNull Plugin plugin, double range) {
        super(plugin);

        this.range = range;
    }

    @Override
    protected @Nullable Object onPacketReceiveAsync(@Nullable Player sender, @NotNull Channel channel, @NotNull Object packet) {
        if (sender == null)
            return packet;
        final String packetName = packet.getClass().getSimpleName();

        if (packetName.equals("PacketPlayInArmAnimation")) {
            Object hand = getField(packet, "a");
            if (hand == null || !hand.toString().equals("MAIN_HAND"))
                return packet;

            // We need to run this synchronously because bukkit sucks
            Bukkit.getScheduler().runTask(getPlugin(), () -> {
                Entity entity = getTarget(sender);
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
     * Approximative re-implementation of vanilla
     * behaviour when trying to hit an entity.
     */
    private @Nullable Entity getTarget(Player player) {
        final RayTraceResult result = player.getWorld().rayTrace(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                range,
                FluidCollisionMode.NEVER,
                true,
                0,
                entity -> entity instanceof LivingEntity && !entity.equals(player));
        if (result == null) return null;
        final Entity entity = result.getHitEntity();
        if (entity == null || entity instanceof Player && ((Player) entity).getGameMode().ordinal() == 3) return null;
        return entity;
    }

    private void triggerEvent(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerInteractEvent event = new PlayerInteractEvent(player, Action.LEFT_CLICK_AIR, player.getInventory().getItemInMainHand(), null, BlockFace.EAST, EquipmentSlot.HAND);
                Bukkit.getPluginManager().callEvent(event);
            }
        }.runTask(getPlugin());
    }

    @Nullable
    private Object getField(Object object, String field) {
        return getField(object, object.getClass(), field);
    }

    @Nullable
    private Object getField(Object object, Class<?> c, String field) {
        if (fields.containsKey(c.getCanonicalName())) {
            Map<String, Field> fs = fields.get(c.getCanonicalName());
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
        if (fields.containsKey(c.getCanonicalName())) {
            map = fields.get(c.getCanonicalName());
        } else {
            map = new ConcurrentHashMap<>();
            fields.put(c.getCanonicalName(), map);
        }

        map.put(f.getName(), f);

        try {
            return f.get(object);
        } catch (ReflectiveOperationException e) {
            return null;
        }

    }
}
