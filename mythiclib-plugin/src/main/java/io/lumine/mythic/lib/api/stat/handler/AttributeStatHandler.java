package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.stat.StatInstance;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class AttributeStatHandler extends StatHandler {
    private final Attribute attribute;

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

        final double mmo = instance.getTotal();
        final double base = instance.getMap().getPlayerData().getPlayer().getAttribute(attribute).getBaseValue();

        /*
         * Only add an attribute modifier if the very final stat
         * value is different from the main one to save calculations.
         */
        if (mmo != base)
            attrIns.addModifier(new AttributeModifier("mythiclib.main", mmo - base, AttributeModifier.Operation.ADD_NUMBER));
    }

    @Override
    public double getBaseValue(@NotNull StatInstance instance) {
        return super.getBaseValue(instance) + instance.getMap().getPlayerData().getPlayer().getAttribute(attribute).getBaseValue();
    }

    @Override
    public double getFinalValue(@NotNull StatInstance instance) {
        return instance.getMap().getPlayerData().getPlayer().getAttribute(attribute).getValue();
    }
}
