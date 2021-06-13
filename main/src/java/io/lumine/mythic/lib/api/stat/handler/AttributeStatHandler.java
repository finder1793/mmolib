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
    public void updateStatMap(StatMap stats) {
        if (!stats.getPlayerData().isOnline()) return;
        AttributeInstance ins = stats.getPlayerData().getPlayer().getAttribute(attribute);
        removeModifiers(ins);

        /*
         * if the attribute is a default attribute, substract default value from
         * it so that it compensates it
         */
        StatInstance statIns = stats.getInstance(stat);
        double d = statIns.getTotal();
        if (d != statIns.getBase())
            ins.addModifier(new AttributeModifier("mythiclib.main", d - statIns.getBase(), AttributeModifier.Operation.ADD_NUMBER));
    }

    @Override
    public double getBaseStatValue(StatMap map) {
        return 0;
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
