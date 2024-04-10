package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.StatInstance;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class AttributeStatHandler extends StatHandler {
    protected final Attribute attribute;

    protected static final String ATTRIBUTE_NAME = "mythiclib.main";
    protected static final double EPSILON = .0001;

    /**
     * Statistics like Atk Damage, Atk Speed, Max Health...
     * which are based on vanilla player attributes.
     *
     * @param config    The ROOT configuration file!
     * @param attribute The corresponding vanilla player attribute
     * @param stat      The stat identifier
     */
    public AttributeStatHandler(ConfigurationSection config, @NotNull Attribute attribute, @NotNull String stat) {
        super(config, stat);

        this.attribute = attribute;
    }

    @Override
    public void runUpdate(@NotNull StatInstance instance) {
        final AttributeInstance attrIns = instance.getMap().getPlayerData().getPlayer().getAttribute(attribute);
        removeModifiers(attrIns);

        final double vanillaBase = instance.getMap().getPlayerData().getPlayer().getAttribute(attribute).getBaseValue();
        final double mmoFinal = clampValue(instance.getFilteredTotal(vanillaBase + this.baseValue, EquipmentSlot.MAIN_HAND::isCompatible));
        final double difference = mmoFinal - vanillaBase;

        /*
         * Only add an attribute modifier if the very final stat
         * value is different from the main one to save map updates.
         */
        if (Math.abs(difference) > EPSILON)
            attrIns.addModifier(new AttributeModifier(ATTRIBUTE_NAME, difference, AttributeModifier.Operation.ADD_NUMBER));
    }

    @Override
    public double getBaseValue(@NotNull StatInstance instance) {
        return this.baseValue + instance.getMap().getPlayerData().getPlayer().getAttribute(attribute).getBaseValue();
    }

    @Override
    public double getFinalValue(@NotNull StatInstance instance) {
        return instance.getMap().getPlayerData().getPlayer().getAttribute(attribute).getValue();
    }

    protected void removeModifiers(@NotNull AttributeInstance ins) {
        for (AttributeModifier attribute : ins.getModifiers())
            if (attribute.getName().equals(ATTRIBUTE_NAME)) ins.removeModifier(attribute);
    }
}
