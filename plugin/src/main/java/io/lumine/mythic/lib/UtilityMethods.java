package io.lumine.mythic.lib;

import io.lumine.mythic.lib.api.MMOLineConfig;
import io.lumine.mythic.lib.api.condition.RegionCondition;
import io.lumine.mythic.lib.api.condition.type.MMOCondition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UtilityMethods {
    /**
     * NOT FINAL CODE.
     * THIS WILL BE MASSIVELY REWORKED VERY SOON!
     */
    public static MMOCondition getCondition(String input) {
        MMOLineConfig config = new MMOLineConfig(input);
        String key = config.getKey().toLowerCase();
        switch (key) {
            case "region":
                if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard"))
                    return null;
                return new RegionCondition(config);
        }

        return null;
    }

    private static final Random RANDOM = new Random();

    /**
     * Super useful to display enum names like DIAMOND_SWORD in chat
     *
     * @param input String with lower cases and spaces only
     * @return Same string with capital letters at the beginning of each word.
     */
    public static String caseOnWords(String input) {
        StringBuilder builder = new StringBuilder(input);
        boolean isLastSpace = true;
        for (int i = 0; i < builder.length(); i++) {
            char ch = builder.charAt(i);
            if (isLastSpace && ch >= 'a' && ch <= 'z') {
                builder.setCharAt(i, (char) (ch + ('A' - 'a')));
                isLastSpace = false;
            } else isLastSpace = ch == ' ';
        }
        return builder.toString();
    }

    public static void dropItemNaturally(Location loc, ItemStack stack) {
        double dx = ((RANDOM.nextFloat() * 0.5F) + 0.25D) / 10;
        double dy = ((RANDOM.nextFloat() * 0.5F) + 0.25D) / 10;
        double dz = ((RANDOM.nextFloat() * 0.5F) + 0.25D) / 10;
        loc.getWorld().dropItem(loc.add(0.5, 0.5, 0.5), stack).setVelocity(new Vector(dx, dy, dz));
    }

    /**
     * Used to find players in chunks around some location. This is
     * used when displaying individual holograms to a list of players.
     *
     * @param loc Target location
     * @return Players in chunks around the location
     */
    public static List<Player> getNearbyPlayers(Location loc) {
        List<Player> players = new ArrayList<>();

        int cx = loc.getChunk().getX();
        int cz = loc.getChunk().getZ();

        for (int x = -1; x < 2; x++)
            for (int z = -1; z < 2; z++)
                for (Entity target : loc.getWorld().getChunkAt(cx + x, cz + z).getEntities())
                    if (target instanceof Player)
                        players.add((Player) target);

        return players;
    }

    /**
     * @return Upper case string, with spaces and - replaced by _
     */
    public static String enumName(String str) {
        return str.toUpperCase().replace("-", "_").replace(" ", "_");
    }

    public static double[] getYawPitch(Vector axis) {
        double _2PI = 6.283185307179586D;
        double x = axis.getX();
        double z = axis.getZ();

        if (x == 0 && z == 0)
            return new double[]{0, axis.getY() > 0 ? -90 : 90};
        else {
            double theta = Math.atan2(-x, z);
            double yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);
            double xz = Math.sqrt(x * x + z * z);
            double pitch = (float) Math.toDegrees(Math.atan(-axis.getY() / xz));
            return new double[]{yaw, pitch};
        }
    }

    public static Vector rotate(Vector rotated, Vector axis) {
        double[] pitchYaw = getYawPitch(axis);
        return rotate(rotated, pitchYaw[0], pitchYaw[1]);
    }

    public static Vector rotate(Vector rotated, double yaw, double pitch) {
        return rotAxisY(rotAxisX(rotated, pitch), -yaw);
    }

    private static Vector rotAxisX(Vector rotated, double angle) {
        double y = rotated.getY() * Math.cos(angle) - rotated.getZ() * Math.sin(angle);
        double z = rotated.getY() * Math.sin(angle) + rotated.getZ() * Math.cos(angle);
        return rotated.setY(y).setZ(z);
    }

    private static Vector rotAxisY(Vector rotated, double angle) {
        double x = rotated.getX() * Math.cos(angle) + rotated.getZ() * Math.sin(angle);
        double z = rotated.getX() * -Math.sin(angle) + rotated.getZ() * Math.cos(angle);
        return rotated.setX(x).setZ(z);
    }

    public static double getAltitude(Entity entity) {
        return getAltitude(entity.getLocation());
    }

    public static double getAltitude(Location loc) {
        Location moving = loc.clone();
        while (!moving.getBlock().getType().isSolid())
            moving.add(0, -1, 0);

        return loc.getY() - moving.getBlockY() - 1;
    }
}
