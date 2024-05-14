package io.lumine.mythic.lib.util.formula;

import io.lumine.mythic.lib.util.formula.preprocess.ExpressionPreprocessor;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NumericalExpression<C> {

    // Built-in functions
    private static final Function RANDOM_DOUBLE = new Function("random", 0) {
        @Override
        public double apply(double... doubles) {
            return RANDOM.nextDouble();
        }
    };
    private static final Function ATAN2 = new Function("atan2", 2) {
        @Override
        public double apply(double... doubles) {
            return Math.atan2(doubles[0], doubles[1]);
        }
    };
    private static final Function POW = new Function("pow", 2) {
        @Override
        public double apply(double... doubles) {
            return Math.pow(doubles[0], doubles[1]);
        }
    };
    private static final Function MIN = new Function("min", 2) {
        @Override
        public double apply(double... doubles) {
            return Math.min(doubles[0], doubles[1]);
        }
    };
    private static final Function MAX = new Function("max", 2) {
        @Override
        public double apply(double... doubles) {
            return Math.max(doubles[0], doubles[1]);
        }
    };
    private static final Function NON_ZERO = new Function("non_zero", 2) {
        @Override
        public double apply(double... doubles) {
            return doubles[0] == 0 ? doubles[1] : doubles[0];
        }
    };
    private static final Function[] FUNCTIONS = {RANDOM_DOUBLE, ATAN2, POW, MIN, MAX, NON_ZERO};

    private static final Random RANDOM = new Random();

    // Constants
    private static final Map<String, Double> CONSTANTS;

    static {
        final Map<String, Double> constants = new HashMap<>();
        constants.put("PI", Math.PI);
        constants.put("Pi", Math.PI);
        final double phi = .5 * (1 + Math.sqrt(5));
        constants.put("phi", phi);
        constants.put("Phi", phi);
        constants.put("PHI", phi);
        CONSTANTS = Collections.unmodifiableMap(constants);
    }

    @Nullable
    private final Expression precompiled;
    private final String expression;
    private final ExpressionPreprocessor<C> preprocessor;

    public NumericalExpression(@NotNull String expression, @NotNull ExpressionPreprocessor<C> preprocessor) {
        this.expression = expression;
        this.preprocessor = preprocessor;

        // Try to precompile the expression
        Expression expressionObject = null;
        try {
            expressionObject = decorateAndCompile(new ExpressionBuilder(preprocessor.preprocess(expression)));
        } catch (Exception ignored) {
            // Not pre-compilation
        }

        this.precompiled = expressionObject;
    }

    public double evaluate(@NotNull C context) {

        // If the expression is precompiled
        if (precompiled != null) {
            preprocessor.process(precompiled, context);
            return precompiled.evaluate();
        }

        return decorateAndCompile(new ExpressionBuilder(preprocessor.quickProcess(expression, context))).evaluate();
    }

    @NotNull
    private Expression decorateAndCompile(@NotNull ExpressionBuilder builder) {
        return builder
                .implicitMultiplication(false)
                .functions(FUNCTIONS)
                .variables(CONSTANTS.keySet())
                .build()
                .setVariables(CONSTANTS);
    }

    /**
     * This is a very lazy implementation of numerical expression computation.
     * This method both compiles and evaluates the given numerical expression.
     * For better performance, do consider pre-processing and pre-compiling the
     * expression before evaluating it.
     * <p>
     * This method will throw RuntimeExceptions if compilation or evaluation fails.
     *
     * @param expression Numerical expression
     * @return Value of numerical expression
     */
    public static double eval(@NotNull String expression) {
        return new NumericalExpression<Void>(expression, ExpressionPreprocessor.EMPTY).evaluate(null);
    }
}
