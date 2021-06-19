package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class AttributeStatHandler implements StatHandler {
    private final Attribute attribute;
    private final String stat;

    public AttributeStatHandler(Attribute attribute, String stat) {
        this.attribute = attribute;
        this.stat = stat;
    }

    @Override
    public void runUpdate(StatMap stats) {
        if (!stats.getPlayerData().isOnline()) return;
        AttributeInstance ins = stats.getPlayerData().getPlayer().getAttribute(attribute);
        removeModifiers(ins);

        /**
         * If the attribute is a default attribute, substract default value from
         * it so that it compensates it
         */
        StatInstance statIns = stats.getInstance(stat);
        double d = statIns.getTotal();
        if (d != statIns.getBase())
            ins.addModifier(new AttributeModifier("mmolib.main", d - statIns.getBase(), AttributeModifier.Operation.ADD_NUMBER));
    }

    @Override
    public double getBaseValue(StatMap map) {
        if (!map.getPlayerData().isOnline()) return 0;
        AttributeInstance ins = map.getPlayerData().getPlayer().getAttribute(attribute);
        return ins.getBaseValue();
    }
}
