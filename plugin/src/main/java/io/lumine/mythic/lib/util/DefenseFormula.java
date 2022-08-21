package io.lumine.mythic.lib.util;

import io.lumine.mythic.lib.MythicLib;
import org.matheclipse.commons.parser.client.eval.DoubleEvaluator;

import java.util.logging.Level;

public class DefenseFormula {
    public double getAppliedDamage(double defense, double damage) {
        String formula = MythicLib.plugin.getMMOConfig().defenseFormula;
        formula = formula.replace("#defense#", String.valueOf(defense));
        formula = formula.replace("#damage#", String.valueOf(damage));

        try {
            return Math.max(0, new DoubleEvaluator().evaluate(formula));
        } catch (RuntimeException exception) {

            /**
             * Formula won't evaluate if hanging #'s or unparsed placeholders. Send a
             * friendly warning to console and just return the default damage.
             */
            MythicLib.inst().getLogger().log(Level.WARNING, "Could not evaluate defense formula (please check config): " + exception.getMessage());
            return damage;
        }
    }
}