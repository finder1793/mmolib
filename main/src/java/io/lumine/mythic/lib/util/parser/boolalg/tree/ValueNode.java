package io.lumine.mythic.lib.util.parser.boolalg.tree;

public class ValueNode implements TreeNode {
    private final boolean value;

    public ValueNode(boolean value) {
        this.value = value;
    }

    @Override
    public boolean evaluate() {
        return value;
    }
}
