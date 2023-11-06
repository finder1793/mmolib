package io.lumine.mythic.lib.comp.formula;

import bsh.EvalError;
import bsh.Interpreter;
import io.lumine.mythic.lib.MythicLib;
import org.apache.commons.lang3.Validate;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class FormulaParser {
    private final Interpreter interpreter;
    private final List<String> mathFunctions = Arrays.asList("pow", "sqrt", "sin", "cos", "tan", "asin", "acos", "atan", "atan2", "exp", "log", "random", "abs", "max", "min");

    public FormulaParser() {
        interpreter = new Interpreter();
        try {
            interpreter.eval("import java.lang.Math;");
            interpreter.eval("import java.util.Arrays;");
            interpreter.eval("import java.util.List;");
        } catch (EvalError error) {
            throw new RuntimeException(error);
        }
    }

    @Deprecated
    public Object eval(String str) throws EvalError {
        return evaluate(str);
    }

    @NotNull
    public Object evaluate(@NotNull String str) {
        // Enable to use val in [val1,val2,val3,...]
        str = str.replaceAll("\"(.*?)\" in \\[(.*?)\\]", "Arrays.asList(new Object[]{$2}).contains(\"$1\")");
        str = str.replaceAll("(\\d*?) in \\[(.*?)\\]", "Arrays.asList(new Object[]{$2}).contains($1)");

        // Parse random(expr1,expr2) to Math.random() * (expr2 - expr1) + expr1
        str = str.replaceAll("random\\((.*?),(.*?)\\)", "random() * ($2 - $1) + ($1)");

        for (String function : mathFunctions)
            str = str.replace(function + "(", "Math." + function + "(");
        try {
            return interpreter.eval(str);
        } catch (EvalError error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    @NotNull
    @Deprecated
    public Object eval(OfflinePlayer player, String str) throws EvalError {
        return evaluate(MythicLib.plugin.getPlaceholderParser().parse(player, str));
    }

    public double evaluateAsDouble(@NotNull OfflinePlayer player, @NotNull String str) {
        return evaluateAsDouble(MythicLib.plugin.getPlaceholderParser().parse(player, str));
    }

    public double evaluateAsDouble(@NotNull String str) {
        return evaluateAs(str, Number.class).doubleValue();
    }

    public int evaluateAsInt(@NotNull String str) {
        return evaluateAs(str, Number.class).intValue();
    }

    public long evaluateAsLong(@NotNull String str) {
        return evaluateAs(str, Number.class).longValue();
    }

    /**
     * For numeric formulas, this method can return both Integers
     * and Doubles. However, Java doesn't allow casting between these
     * two types. It's always better to go through the Number superclass
     * and get the double/int value based on the desired output.
     * <p>
     * This method will throw a RuntimeException if an evaluation
     * error occurs or if the given object is not of the right type.
     *
     * @param str         Given formula string
     * @param targetClass Class of type of object expected
     * @param <O>         Type of object expected
     * @return Object provided when processing given formula
     */
    @NotNull
    public <O> O evaluateAs(@NotNull String str, @NotNull Class<O> targetClass) {
        final Object result = evaluate(str);
        Validate.isInstanceOf(targetClass, result, "Formula did not return a number");
        return (O) result;
    }
}
