package io.lumine.mythic.lib.api.stat;

import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.player.modifier.ModifierType;

/*
 * Used for percentage-based stats, where 100 are 100% and 0 are 0%, for example Damage Reduction.
 * With this, each FLAT modifier of the stat will not just be added to total value, because it can
 * cause to receive 100% damage reduction, which is not good. The exact formula for this is equal to:
 * (1 - modifier1 / 100) * (1 - modifier2 / 100) * ...
 * The RELATIVE modifiers also have different behaviour: instead of modifying total value, which also
 * often can cause 100% value, the total RELATIVE modifier apply independently for each FLAT modifier.
 * So, exact formula should be:
 * efficiency = relativeModifier1 + relativeModifier2 + ...
 * total = (1 - modifier1 * efficiency / 100) + (1 - modifier2 * efficiency / 100) + ...
 *
 * Example:
 * We have an armor set, that provides us 20% magic damage reduction for every of this parts
 * We also have a shield, that provides us 50% magic damage reduction
 * We received 100 magic damage from enemy wizard, but we receive only...
 * 
 * With OLD StatInstance we receive 0 damage, because total magic reduction is 130%
 * 
 * With PercentageStatInstance we receive 20,48 damage, because total magic reduction is 79,52%
 * 
 * For what purpose, if we just can doesn't allow players to get that much reduction from items? Its
 * relatively easy to keep up limitations, when we have only 6 equipment slots (armor, main and off hand),
 * but when we start using things like MMOBuffs, MMOInventory or skill trees in MMOCore, we either have
 * to set this stat to very low values or don't use this stat in some of the equipment, because its can
 * break balance.
 * 
 * Formula inspired by Dota 2 Magic Reduction.
 * Sorry for bad english.
 */
public class PercentageStatInstance extends StatInstance {

    public PercentageStatInstance(@NotNull StatMap map, @NotNull String stat) {
        super(map, stat);
    }

    @Override
    public double getFilteredTotal(double d, Predicate<StatModifier> filter, Function<StatModifier, StatModifier> modification) {
        double efficiency = 1;

        for (StatModifier mod : modifiers.values())
            if (mod.getType() == ModifierType.RELATIVE && filter.test(mod))
                efficiency *= 1 + modification.apply(mod).getValue() / 100;

        double threshold = 100;

        for (StatModifier mod : modifiers.values()) {
            if (mod.getType() == ModifierType.FLAT && filter.test(mod)) {
                d += modification.apply(mod).getValue() * efficiency * threshold / 100;
            }
        }

        return d;
    }
    
}
