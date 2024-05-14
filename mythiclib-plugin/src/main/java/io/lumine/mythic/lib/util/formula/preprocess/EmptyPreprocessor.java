package io.lumine.mythic.lib.util.formula.preprocess;

import net.objecthunter.exp4j.Expression;
import org.jetbrains.annotations.NotNull;

public class EmptyPreprocessor implements ExpressionPreprocessor<Void> {

    @Override
    public @NotNull String preprocess(@NotNull String expression) {
        return expression;
    }

    @Override
    public void process(@NotNull Expression expression, @NotNull Void context) {
        // Nothing to do
    }

    @Override
    public @NotNull String quickProcess(@NotNull String expression, @NotNull Void context) {
        return expression;
    }
}
