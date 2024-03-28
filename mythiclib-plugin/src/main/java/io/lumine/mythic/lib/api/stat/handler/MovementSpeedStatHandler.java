package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.StatInstance;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class MovementSpeedStatHandler extends AttributeStatHandler {
    private final boolean moveSpeed;

    /**
     * Move speed and speed malus reduction, which share the same
     * update task, so they are grouped up in the same stat handler.
     *
     * @param config    Root stat handlers config file
     * @param stat      Stat identifier
     * @param moveSpeed Is it move speed?
     */
    public MovementSpeedStatHandler(@NotNull ConfigurationSection config, @NotNull String stat, boolean moveSpeed) {
        super(config, Attribute.GENERIC_MOVEMENT_SPEED, stat);

        this.moveSpeed = moveSpeed;
    }

    @Override
    public void runUpdate(@NotNull StatInstance randomInstance) {
        final AttributeInstance attrIns = randomInstance.getMap().getPlayerData().getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        removeModifiers(attrIns);

        // Calculate speed malus reduction (capped at 80%)
        final double coef = 1 - randomInstance.getMap().getStat("SPEED_MALUS_REDUCTION") / 100;

        final StatInstance statIns = randomInstance.getMap().getInstance("MOVEMENT_SPEED");
        final double vanillaBase = statIns.getMap().getPlayerData().getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        final double mmoFinal = statIns.getFilteredTotal(vanillaBase, EquipmentSlot.MAIN_HAND::isCompatible, mod -> mod.getValue() < 0 ? mod.multiply(coef) : mod);

        /*
         * Only add an attribute modifier if the very final stat
         * value is different from the main one to save calculations.
         */
        if (Math.abs(mmoFinal - vanillaBase) > EPSILON)
            attrIns.addModifier(new AttributeModifier(ATTRIBUTE_NAME, mmoFinal - vanillaBase, AttributeModifier.Operation.ADD_NUMBER));
    }

    @Override
    public double getFinalValue(@NotNull StatInstance instance) {
        return moveSpeed ? instance.getMap().getPlayerData().getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue() : instance.getTotal();
    }

    @Override
    public double getBaseValue(@NotNull StatInstance instance) {
        // TODO support configurable base value for SMR and MS
        return moveSpeed ? instance.getMap().getPlayerData().getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() : 0;
    }
}
