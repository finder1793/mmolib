package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

import java.util.function.Consumer;

public class MovementSpeedStatHandler implements Consumer<StatMap> {

    @SuppressWarnings("deprecation")
    @Override
    public void accept(StatMap stats) {
        AttributeInstance ins = stats.getPlayerData().getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        removeModifiers(ins);

        /*
         * calculate speed malus reduction (capped at 80%)
         */
        double coef = 1 - Math.min(.8, Math.max(0, stats.getInstance("SPEED_MALUS_REDUCTION").getTotal() / 100));

        // fix player walk speed


        if (AttributeStatHandler.updateAttributes) {
            if (Bukkit.isPrimaryThread())
                stats.getPlayerData().getPlayer().setWalkSpeed(.2f);
            else Bukkit.getScheduler().runTask(MythicLib.plugin, () ->
                    stats.getPlayerData().getPlayer().setWalkSpeed(.2f));
        }

        /*
         * unlike other attributes, MMOLib directly applies movement speed as
         * base value which is an important compatibility issue, can't see
         * anything better as of right now
         */
        ins.setBaseValue(stats.getInstance("MOVEMENT_SPEED").getTotal(mod -> mod.getValue() < 0 ? mod.multiply(coef) : mod));
    }

    /*
     * TODO remove mmoitems. in 1 year when corrupted data is gone
     */
    private void removeModifiers(AttributeInstance ins) {
        for (AttributeModifier attribute : ins.getModifiers()) {
            if (attribute.getName().startsWith("mmolib.") || attribute.getName().startsWith("mmoitems."))
                ins.removeModifier(attribute);
        }
    }
}
