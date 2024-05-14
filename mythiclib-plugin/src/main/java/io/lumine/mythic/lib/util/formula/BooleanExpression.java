package io.lumine.mythic.lib.util.formula;

import io.lumine.mythic.lib.util.formula.preprocess.ExpressionPreprocessor;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BooleanExpression<C> {

    // Logical operators
    private static final int EQUALITY_PRECEDENCE = 100;
    private static final Operator LOGICAL_AND = new Operator("&&", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {

        @Override
        public double apply(double... args) {
            return ((args[0] != 0) && (args[1] != 0)) ? 1 : 0;
        }
    };
    private static final Operator LOGICAL_OR = new Operator("||", 2, true, Operator.PRECEDENCE_ADDITION) {

        @Override
        public double apply(double... args) {
            return ((args[0] != 0) || (args[1] != 0)) ? 1 : 0;
        }
    };
    private static final Operator LOGICAL_NOT = new Operator("!", 1, true, Operator.PRECEDENCE_ADDITION) {

        @Override
        public double apply(double... args) {
            return args[0] != 0 ? 0 : 1;
        }
    };
    private static final Operator LOGICAL_EQ = new Operator("==", 2, true, EQUALITY_PRECEDENCE) {

        @Override
        public double apply(double... args) {
            return Math.abs(args[0] - args[1]) < .5 ? 1 : 0;
        }
    };
    private static final Operator LOGICAL_NEQ = new Operator("!=", 2, true, EQUALITY_PRECEDENCE) {

        @Override
        public double apply(double... args) {
            return Math.abs(args[0] - args[1]) > .5 ? 1 : 0;
        }
    };
    private static final Operator[] OPERATORS = {LOGICAL_NOT, LOGICAL_OR, LOGICAL_AND, LOGICAL_EQ, LOGICAL_NEQ};

    // Logical constants
    private static final String[] CONSTANTS = {"true", "false"};

    @Nullable
    private final Expression precompiled;
    private final String expression;
    private final ExpressionPreprocessor<C> preprocessor;

    public BooleanExpression(@NotNull String expression, @NotNull ExpressionPreprocessor<C> preprocessor) {
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

    public boolean evaluate(@NotNull C context) {

        // If the expression is precompiled
        if (precompiled != null) {
            preprocessor.process(precompiled, context);
            return precompiled.evaluate() != 0;
        }

        return decorateAndCompile(new ExpressionBuilder(preprocessor.quickProcess(expression, context))).evaluate() != 0;
    }

    @NotNull
    private Expression decorateAndCompile(@NotNull ExpressionBuilder builder) {
        return builder
                .implicitMultiplication(false)
                .operator(OPERATORS)
                .variables(CONSTANTS)
                .build() // Heavy expression compilation call
                .setVariable("true", 1).setVariable("false", 0);
    }

    public static boolean eval(@NotNull String expression) {
        return new BooleanExpression<Void>(expression, ExpressionPreprocessor.EMPTY).evaluate(null);
    }
}
