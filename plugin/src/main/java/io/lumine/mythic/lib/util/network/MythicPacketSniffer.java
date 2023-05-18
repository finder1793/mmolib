package io.lumine.mythic.lib.util.network;

import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
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
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mythiclib
 * 18/05/2023
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class MythicPacketSniffer extends LightInjector {

    public static final String COMPLETE_VERSION = Bukkit.getServer().getClass().getName().split("\\.")[3];
    public static final int VERSION = Integer.parseInt(COMPLETE_VERSION.split("_")[1]);
    public static final int RELEASE = Integer.parseInt(COMPLETE_VERSION.split("R")[1]);

    private static final Map<String, Map<String, Field>> fields = new ConcurrentHashMap<>();
    private static final boolean IS_1_17 = VERSION >= 17;


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
        if (!packet.getClass().getSimpleName().equals("PacketPlayInArmAnimation"))
            return packet;
        if (IS_1_17) {
            Object hand = getField(packet, "a");
            if (hand == null || !hand.toString().equals("MAIN_HAND"))
                return packet;
            new BukkitRunnable() {
                @Override
                public void run() {
                    Entity entity = getTarget(sender);
                    if (entity == null)
                        return;
                    PlayerInteractEvent event = new PlayerInteractEvent(sender, Action.LEFT_CLICK_AIR, sender.getInventory().getItemInMainHand(), null, BlockFace.EAST, EquipmentSlot.HAND);
                    Bukkit.getPluginManager().callEvent(event);
                    System.out.println("Entity: " + entity.getType().name());
                }
            }.runTask(getPlugin());
        } else {
            Object entity = getField(packet, "a");
            if (entity != null)
                System.out.println("Entity: " + entity);
            Object type = getField(packet, "b");
            if (type != null)
                System.out.println("Type: " + type);
        }
        return packet;
    }

    @Override
    protected @Nullable Object onPacketSendAsync(@Nullable Player receiver, @NotNull Channel channel, @NotNull Object packet) {
        return packet;
    }

    private @Nullable Entity getTarget(Player player) {
        double range = 3.5;
        return player.getNearbyEntities(range, range, range)
                .stream()
                .filter(entity -> !(entity instanceof Player && ((Player) entity).getGameMode().ordinal() == 3))
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> isLineOfSight(player, entity))
                .min((a, b) -> (int) (a.getLocation().distanceSquared(player.getLocation()) - b.getLocation().distanceSquared(player.getLocation())))
                .orElse(null);
    }

    private boolean isLineOfSight(Player player, Entity targetEntity) {
        Location eyeLocation = player.getEyeLocation();
        Vector playerDirection = eyeLocation.getDirection();
        RayTraceResult result = player.getWorld().rayTrace(eyeLocation, playerDirection, 100, FluidCollisionMode.NEVER, true, 0.1, entity -> entity.equals(targetEntity));
        return result != null && result.getHitEntity() == targetEntity;
    }

    @Nullable
    private Object getField(Class<?> clazz, String field) {
        return getField(null, clazz, field);
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

    @Nullable
    private Object invoke(Object object, String method, Object... parameters) {
        Class<?>[] classes = new Class<?>[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].getClass();
        }

        Method m;
        try {
            m = object.getClass().getDeclaredMethod(method, classes);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
        Object o1;
        try {
            boolean b = m.isAccessible();
            m.setAccessible(true);
            o1 = m.invoke(object, parameters);
            m.setAccessible(b);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
        return o1;
    }
}
