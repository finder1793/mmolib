package io.lumine.mythic.lib.util.parser.boolalg.tree;

import io.lumine.mythic.lib.util.parser.boolalg.Operator;
import io.lumine.mythic.lib.util.parser.boolalg.ParsingException;
import org.apache.commons.lang.Validate;

public class OperatorNode implements TreeNode {
    private final OperatorNode parent;

    public Operator operator;
    public TreeNode child1, child2;

    public OperatorNode(OperatorNode parent) {
        this.parent = parent;
    }

    public OperatorNode getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isFull() {
        return child1 != null && child2 != null;
    }

    public void addNode(TreeNode node) {
        Validate.isTrue(child1 == null || child2 == null, "Cannot have three operands");

        if (child1 == null)
            child1 = node;
        else if (child2 == null)
            child2 = node;
        else
            throw new ParsingException("Tree node is full, uses parentheses");
    }

    @Override
    public boolean evaluate() {
        return operator.test(child1.evaluate(), child2.evaluate());
    }
}
