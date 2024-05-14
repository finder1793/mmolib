package io.lumine.mythic.lib.util.formula.preprocess;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.util.annotation.NotUsed;
import net.objecthunter.exp4j.Expression;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A very important class for the whole pre-compilation problem. This
 * is the class that actually does all the preprocessing, enabling
 * expression pre-compilation in most cases.
 * <p>
 * A preprocessor for pre-compiled expression that supports:
 * - internal placeholders like {placeholder_name} (with arbitrary start and end characters)
 * - non-nested PlaceholderAPI placeholders
 *
 * @author Jules
 */
@NotUsed
@Deprecated
public abstract class PlaceholderPreprocessor<T> implements ExpressionPreprocessor<T> {
    private final Pattern placeholderPattern;
    private static final char PAPI = '%';

    private Map<String, String> internal = new HashMap<>();
    private Map<String, String> papi = new HashMap<>();
    private int variableCount;

    public PlaceholderPreprocessor(char start, char end) {
        this.placeholderPattern = UtilityMethods.internalPlaceholderPattern(start, end);
    }

    private String newVariableName() {
        return "var_" + variableCount++;
    }

    @Override
    public @NotNull String preprocess(@NotNull String expression) {

        /*
        // Internal placeholders
        Matcher match = placeholderPattern.matcher(expression);
        while (match.find()) {
            final String placeholder = expression.substring(match.start(), match.end());
            final String variableName = newVariableName();
            expression = expression.replace(placeholder, variableName); // Replace by readable variable name
            internal.put(placeholder, variableName);
            match = placeholderPattern.matcher(expression);
        }

        final char[] arr = expression.toCharArray();

        PlaceholderAPI.setPlaceholders()*/



        return expression;
    }

    @Override
    public void process(@NotNull Expression expression, @NotNull T context) {

    }
}
