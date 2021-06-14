package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

import java.util.function.Consumer;

public class AttributeStatHandler implements Consumer<StatMap> {
    private final Attribute attribute;
    private final String stat;

    @Deprecated
    public static boolean updateAttributes;

    public AttributeStatHandler(Attribute attribute, String stat) {
        this.attribute = attribute;
        this.stat = stat;
    }

    @Override
    public void accept(StatMap stats) {
        if(!stats.getPlayerData().isOnline()) return;
        AttributeInstance ins = stats.getPlayerData().getPlayer().getAttribute(attribute);
        removeModifiers(ins);

        if (updateAttributes)
            ins.setBaseValue(MythicLib.plugin.getStats().getBaseValue(stat));

        /*
         * if the attribute is a default attribute, substract default value from
         * it so that it compensates it
         */
        StatInstance statIns = stats.getInstance(stat);
        double d = statIns.getTotal();
        if (d != statIns.getBase())
            ins.addModifier(new AttributeModifier("mmolib.main", d - statIns.getBase(), AttributeModifier.Operation.ADD_NUMBER));
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
