package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.stat.provider.StatProvider;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.element.Element;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.trigger.TriggerMetadata;
import io.lumine.mythic.lib.util.DefenseFormula;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

/**
 * Class which implements the elemental damage calculation
 * AND application. This also applies elemental critical
 * strikes.
 *
 * @author indyuce
 */
public class ElementalDamage implements Listener {
    private static final Random RANDOM = new Random();

    @EventHandler
    public void applyElementalDamage(PlayerAttackEvent event) {

        // Elemental damage only applies on weapon damage
        if (!event.getDamage().hasType(DamageType.WEAPON))
            return;

        final double critChance = Math.min(event.getAttack().getStat("CRITICAL_STRIKE_CHANCE"), MythicLib.plugin.getAttackEffects().getMaxWeaponCritChance());
        for (Element element : MythicLib.plugin.getElements().getAll()) {

            // If the flat damage is 0; cancel everything asap
            final StatProvider attackerStats = event.getAttack();
            double damage = attackerStats.getStat(element.getId() + "_DAMAGE");
            if (damage == 0)
                continue;

            // Multiply flat damage by the percent based stat
            final double percentDamage = attackerStats.getStat(element.getId() + "_DAMAGE_PERCENT");
            damage *= 1 + Math.max(-1, percentDamage / 100);
            if (damage == 0)
                continue;

            // Apply elemental weakness
            final StatProvider opponentStats = StatProvider.get(event.getEntity());
            final double weakness = opponentStats.getStat(element.getId() + "_WEAKNESS");
            damage *= 1 + Math.max(-1, weakness / 100);
            if (damage == 0)
                continue;

            // Apply elemental defense
            double defense = opponentStats.getStat(element.getId() + "_DEFENSE");
            defense *= 1 + Math.max(-1, opponentStats.getStat(element.getId() + "_DEFENSE_PERCENT") / 100);
            damage = new DefenseFormula().getAppliedDamage(defense, damage);

            // Register the damage packet
            event.getDamage().add(damage, element);

            // Apply critical strikes
            final boolean crit = RANDOM.nextDouble() < critChance / 100;
            Skill skill = element.getSkill(crit);
            if (skill != null)
                skill.cast(new TriggerMetadata(event.getAttack(), event.getEntity()));
            if (crit)
                event.getDamage().registerElementalCriticalStrike(element);
        }
    }
}
