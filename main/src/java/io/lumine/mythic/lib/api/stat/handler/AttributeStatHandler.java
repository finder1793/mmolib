package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.api.stat.modifier.ModifierSource;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class AttributeStatHandler implements StatHandler {
    private final Attribute attribute;
    private final String stat;

    /**
     * Stats like Attack Damage and Attack Speed from off hand
     * item which must not stack with main hand item stats
     */
    private final boolean weaponStat;

    public AttributeStatHandler(Attribute attribute, String stat, boolean weaponStat) {
        this.attribute = attribute;
        this.stat = stat;
        this.weaponStat = weaponStat;
    }

    @Override
    public void runUpdate(StatMap stats) {
        if (!stats.getPlayerData().isOnline())
            return;

        AttributeInstance ins = stats.getPlayerData().getPlayer().getAttribute(attribute);
        removeModifiers(ins);

        /**
         * If the attribute is a default attribute, substract default value from
         * it so that it compensates it
         */
        StatInstance statIns = stats.getInstance(stat);
        double d = statIns.getFilteredTotal(mod -> mod.getSource() != ModifierSource.RANGED_WEAPON &&
                (mod.getSource() != ModifierSource.MELEE_WEAPON || mod.getSlot() != EquipmentSlot.OFF_HAND));
        double base = statIns.getBase();

        if (d != base)
            ins.addModifier(new AttributeModifier("mmolib.main", d - base, AttributeModifier.Operation.ADD_NUMBER));
    }

    @Override
    public double getBaseValue(StatMap map) {
        return map.getPlayerData().isOnline() ? map.getPlayerData().getPlayer().getAttribute(attribute).getBaseValue() : 0;
    }
}
