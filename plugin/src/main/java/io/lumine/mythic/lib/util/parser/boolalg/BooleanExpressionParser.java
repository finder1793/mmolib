package io.lumine.mythic.lib.util.parser.boolalg;

import io.lumine.mythic.lib.util.parser.boolalg.tree.OperatorNode;
import io.lumine.mythic.lib.util.parser.boolalg.tree.ValueNode;
import org.apache.commons.lang.Validate;

/**
 * Parses a boolean expression like "(!true && false) || false"
 * using a tree. The first step is to convert the expression into
 * a tree with nodes.
 * <p>
 * Every node contains two operands (which are two other nodes)
 * as well as one comparator.
 * <p>
 * When the tree is finally constructed, every node is evaluated
 * to provide the final expression result.
 * <p>
 * Time and space complexity is O(n)
 *
 * @author jules
 */
public class BooleanExpressionParser {

    public boolean evaluate(String input) {

        // Trivial values
        if (input.equals("true"))
            return true;

        if (input.equals("false"))
            return false;

        OperatorNode current = new OperatorNode(null);

        int i = 0;
        while (i < input.length())
            try {
                switch (input.charAt(i)) {

                    // Do nothing
                    case ' ': {
                        i++;
                        break;
                    }

                    // Add simple true node
                    case 't': {
                        do
                            i++;
                        while (i < input.length() && (input.charAt(i) == 'r' || input.charAt(i) == 'u' || input.charAt(i) == 'e'));
                        current.addNode(new ValueNode(true));
                        break;
                    }

                    // Add simple false node
                    case 'f': {
                        do
                            i++;
                        while (i < input.length() && (input.charAt(i) == 'a' || input.charAt(i) == 'l' || input.charAt(i) == 's' || input.charAt(i) == 'e'));
                        current.addNode(new ValueNode(true));
                        break;
                    }

                    // AND operand
                    case '&':
                    case '*': {
                        do
                            i++;
                        while (i < input.length() && (input.charAt(i) == '&' || input.charAt(i) == '*'));

                        current.operator = Operator.AND;
                        break;
                    }

                    // OR operand
                    case '|':
                    case '+': {
                        do
                            i++;
                        while (i < input.length() && (input.charAt(i) == '|' || input.charAt(i) == '+'));

                        current.operator = Operator.OR;
                        break;
                    }

                    // Add new tree floor
                    case '(': {
                        i++;

                        OperatorNode node = new OperatorNode(current);
                        current.addNode(node);
                        current = node;
                        break;
                    }

                    // Go up a tree floor
                    case ')': {
                        i++;

                        Validate.isTrue(!current.isRoot(), "Missing a ( parenthesis");
                        Validate.isTrue(current.isFull(), "Missing an operand");
                        current = current.getParent();
                        break;
                    }

                    default:
                        throw new ParsingException("Cannot recognize operator or operand");
                }
            } catch (RuntimeException exception) {
                throw new ParsingException("Error at char " + i + ": " + exception.getMessage());
            }

        Validate.isTrue(current.isRoot(), "Missing a ) parenthesis");
        Validate.isTrue(current.isFull(), "Missing an operand");
        return current.evaluate();
    }
}
