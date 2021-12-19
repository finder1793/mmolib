package io.lumine.mythic.lib.util.parser.boolalg;

import java.util.function.BiPredicate;

public enum Operator {
    AND("&&", (b1, b2) -> b1 && b2),
    OR("||", (b1, b2) -> b1 || b2);

    private final String str;
    private final BiPredicate<Boolean, Boolean> behaviour;

    Operator(String str, BiPredicate<Boolean, Boolean> behaviour) {
        this.str = str;
        this.behaviour = behaviour;
    }

    public boolean test(boolean b1, boolean b2) {
        return behaviour.test(b1, b2);
    }

    @Override
    public String toString() {
        return str;
    }
}
