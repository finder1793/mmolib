package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.stat.provider.StatProvider;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.element.Element;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.trigger.TriggerMetadata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

/**
 * Class which implements the elemental damage calculation
 * AND application. This also applies elemental critical
 * strikes.
 * <p>
 * Extra stats which could be implemented in the future
 * - Flat Elemental Damage Reduction
 * - % Elemental Damage Reduction
 *
 * @author indyuce
 */
public class ElementalDamage implements Listener {
    private static final Random random = new Random();

    @EventHandler
    public void applyElementalDamage(PlayerAttackEvent event) {

        // Elemental damage only applies on weapon damage
        if (!event.getDamage().hasType(DamageType.WEAPON))
            return;

        double critChance = Math.min(event.getAttack().getStat("CRITICAL_STRIKE_CHANCE"), MythicLib.plugin.getAttackEffects().getMaxWeaponCritChance());
        for (Element element : MythicLib.plugin.getElements().getAll()) {

            // If the flat damage is 0; cancel everything asap
            StatProvider attackerStats = event.getAttack();
            double damage = attackerStats.getStat(element.getUpperCaseId() + "_DAMAGE");
            if (damage == 0)
                continue;

            // Multiply flat damage by the percent based stat
            double percentDamage = attackerStats.getStat(element.getUpperCaseId() + "_DAMAGE_PERCENT");
            damage *= 1 + Math.max(-1, percentDamage / 100);
            if (damage == 0)
                continue;

            // Apply elemental weakness
            StatProvider opponentStats = StatProvider.get(event.getEntity());
            double weakness = opponentStats.getStat(element.getUpperCaseId() + "_WEAKNESS");
            damage *= 1 + Math.max(-1, weakness / 100);
            if (damage == 0)
                continue;

            // Apply elemental defense
            double defense = opponentStats.getStat(element.getUpperCaseId() + "_DEFENSE");
            defense *= 1 + Math.max(-1, opponentStats.getStat(element.getUpperCaseId() + "_DEFENSE_PERCENT") / 100);
            damage = MythicLib.plugin.getMMOConfig().getAppliedElementalDamage(damage, defense);

            // Register the damage packet
            event.getDamage().add(damage, element, DamageType.WEAPON);

            // Apply critical strikes
            Skill skill = element.getSkill(random.nextDouble() < critChance / 100);
            if (skill != null)
                skill.cast(new TriggerMetadata(event.getAttack(), event.getEntity()));
        }
    }
}
