package io.lumine.mythic.lib.util.formula.preprocess;

import net.objecthunter.exp4j.Expression;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * The fundamental issue with expressions is how to precompile them.
 * An expression preprocessor does its best at turning a formula
 * containing placeholders (which a user would input in the config)
 * into an expression containing only variable names which can then
 * be pre-compiled by the exp4j expression builder. This is done with
 * the {@link #preprocess(String)} method.
 * <p>
 * If successfully preprocessed, exp4j will precompile the expression,
 * and only the {@link #process(Expression, Object)} method will be
 * called when evaluating the expression, as to minimize impact on performance.
 * <p>
 * If the preprocessor implementation is not strong enough, it will not
 * preprocess placeholders (nested placeholders are very hard to deal with)
 * and exp4j will throw an error when trying to precompile the expression.
 * The expression will not be pre-compiled, and exp4j will compile it
 * right before evaluating it. This is done with the {@link #quickProcess(String, Object)}
 * method.
 * <p>
 * Either way, only one of these two methods will be called when evaluating
 * an expression.
 *
 * @author Jules
 */
public interface ExpressionPreprocessor<C> {

    @NotNull
    public String preprocess(@NotNull String expression);

    public void process(@NotNull Expression expression, @NotNull C context);

    @NotNull
    public String quickProcess(@NotNull String expression, @NotNull C context);

    public static final ExpressionPreprocessor<Void> EMPTY = new EmptyPreprocessor();
    public static final ExpressionPreprocessor<OfflinePlayer> PLAYER = new PlayerPreprocessor();
}
