package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class MovementSpeedStatHandler implements StatHandler {

    @SuppressWarnings("deprecation")
    @Override
    public void runUpdate(StatMap stats) {
        AttributeInstance ins = stats.getPlayerData().getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        removeModifiers(ins);

        /**
         * Calculate speed malus reduction (capped at 80%)
         */
        double coef = 1 - Math.min(.8, Math.max(0, stats.getInstance("SPEED_MALUS_REDUCTION").getTotal() / 100));

        /**
         * Unlike other attributes, MMOLib directly applies movement speed as
         * base value which is an important compatibility issue, can't see
         * anything better as of right now
         */
        ins.setBaseValue(stats.getInstance("MOVEMENT_SPEED").getTotal(mod -> mod.getValue() < 0 ? mod.multiply(coef) : mod));
    }

    /**
     * No choice but to impose the default movement speed
     */
    @Override
    public double getBaseValue(StatMap map) {
        return .1;
    }
}
