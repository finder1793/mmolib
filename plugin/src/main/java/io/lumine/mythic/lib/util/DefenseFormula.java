package io.lumine.mythic.lib.util;

import io.lumine.mythic.lib.MythicLib;
import org.matheclipse.commons.parser.client.eval.DoubleEvaluator;

import java.util.logging.Level;

public class DefenseFormula {
    private final String formula;

    public DefenseFormula(boolean elemental) {
        this.formula = elemental ? MythicLib.plugin.getMMOConfig().elementalDefenseFormula : MythicLib.plugin.getMMOConfig().naturalDefenseFormula;
    }

    public double getAppliedDamage(double defense, double damage) {
        String expression = this.formula;
        expression = expression.replace("#defense#", String.valueOf(defense));
        expression = expression.replace("#damage#", String.valueOf(damage));

        try {
            return Math.max(0, new DoubleEvaluator().evaluate(expression));
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