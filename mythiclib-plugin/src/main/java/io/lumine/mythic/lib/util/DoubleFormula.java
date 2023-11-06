package io.lumine.mythic.lib.util;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.skill.SkillMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * Instead of directly using a double in a skill, we rather use a string
 * where internal or PAPI placeholders are parsed before finally
 * evaluating the formula.
 * <p>
 * This represents 90% of the total skill system configurability
 */
public class DoubleFormula {
    @Nullable
    private final String value;
    private final double trivialValue;
    private final boolean trivial;

    public static final DoubleFormula ZERO = new DoubleFormula(0);

    public DoubleFormula(@NotNull String value) {
        this.value = value;

        double trivialValue;
        boolean trivial;

        try {
            trivialValue = Double.valueOf(value);
            trivial = true;
        } catch (IllegalArgumentException exception) {
            trivialValue = 0;
            trivial = false;
        }

        this.trivialValue = trivialValue;
        this.trivial = trivial;
    }

    /**
     * A mere double
     */
    public DoubleFormula(double trivialValue) {
        this.trivial = true;
        this.trivialValue = trivialValue;
        this.value = null;
    }

    public double evaluate(@NotNull SkillMetadata meta) {
        try {
            return trivial ? trivialValue : MythicLib.plugin.getFormulaParser().evaluateAsDouble(meta.parseString(value));
        } catch (Exception exception) {
            MythicLib.plugin.getLogger().log(Level.WARNING, "Could not evaluate '" + value + "' while casting skill '" + meta.getCast().getHandler().getId() + "': " + exception.getMessage());
            return 0;
        }
    }
}
