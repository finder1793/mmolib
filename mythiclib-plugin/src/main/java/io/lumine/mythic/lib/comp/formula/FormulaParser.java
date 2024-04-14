package io.lumine.mythic.lib.comp.formula;

import bsh.EvalError;
import bsh.Interpreter;
import io.lumine.mythic.lib.MythicLib;
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
            //interpreter.eval("import java.util.Arrays;");
            //interpreter.eval("import java.util.List;");
            interpreter.eval("import java.util.Random;");

            interpreter.eval("private static final Random RANDOM = new Random();");

            // Constants
            interpreter.eval("private static final double PI = Math.PI, Pi = Math.PI;");

            // random(double, double) function definition
            interpreter.eval("private static final double random(double d1, double d2) { return RANDOM.nextDouble() * (d2 - d1) + d1; }");

            // Math function definitions
            for (String mathFunction : mathFunctions)
                interpreter.eval("private static final double " + mathFunction + "(double d) { return Math." + mathFunction + "(d); }");
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
        // Removed to avoid performance loss
        // str = str.replaceAll("\"(.*?)\" in \\[(.*?)\\]", "Arrays.asList(new Object[]{$2}).contains(\"$1\")");
        // str = str.replaceAll("(\\d*?) in \\[(.*?)\\]", "Arrays.asList(new Object[]{$2}).contains($1)");

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
     * For numeric expressions, this method can return both Integers
     * and Doubles. However, Java doesn't allow casting between these
     * two types. It's always better to go through the Number superclass
     * and get the double/int value based on the desired output.
     * <p>
     * This method will throw a RuntimeException if an evaluation
     * error occurs or if the given object is not of the right type.
     *
     * @param str         Given expression
     * @param targetClass Class of type of object expected
     * @param <O>         Type of object expected
     * @return Object provided when processing given expression
     */
    @NotNull
    public <O> O evaluateAs(@NotNull String str, @NotNull Class<O> targetClass) {
        final Object result = evaluate(str);
        if (!targetClass.isInstance(result))
            throw new IllegalArgumentException("Expression did not return a " + targetClass.getSimpleName());
        return (O) result;
    }
}
