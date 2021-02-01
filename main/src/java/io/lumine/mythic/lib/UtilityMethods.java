package io.lumine.mythic.lib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class UtilityMethods {
    /**
     * NOT FINAL CODE.
     * THIS WILL BE MASSIVELY REWORKED VERY SOON!
     */
    public static MMOCondition getCondition(String input) {
        MMOLineConfig config = new MMOLineConfig(input);
        String key = config.getKey().toLowerCase();
        switch(key) {
            case "region":
                if(!Bukkit.getPluginManager().isPluginEnabled("WorldGuard"))
                    return null;
                return new RegionCondition(config);
        }

        return null;
    }

    private static final Random RANDOM = new Random();

    public static void dropItemNaturally(Location loc, ItemStack stack) {
        double dx = ((RANDOM.nextFloat() * 0.5F) + 0.25D) / 10;
        double dy = ((RANDOM.nextFloat() * 0.5F) + 0.25D) / 10;
        double dz = ((RANDOM.nextFloat() * 0.5F) + 0.25D) / 10;
        loc.getWorld().dropItem(loc.add(0.5, 0.5, 0.5), stack).setVelocity(new Vector(dx, dy, dz));
    }
}
