package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class MovementSpeedStatHandler implements StatHandler {

    @Override
    public void updateStatMap(StatMap stats) {
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
     * Right now we impose to all users a base
     * movement speed of 0.2 but that is a TODO
     */
    @Override
    public double getBaseStatValue(StatMap map) {
        return .1;
    }

    /**
     * CAUTION to keep PlayerDatas a little cleaner and even if the lib name was
     * changed we should keep the attribute modifier name to mmolib
     */
    private void removeModifiers(AttributeInstance ins) {
        for (AttributeModifier attribute : ins.getModifiers())
            if (attribute.getName().startsWith("mmolib.") || attribute.getName().startsWith("mmoitems."))
                ins.removeModifier(attribute);
    }
}
