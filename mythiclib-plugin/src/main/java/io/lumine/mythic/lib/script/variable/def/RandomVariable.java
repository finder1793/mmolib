package io.lumine.mythic.lib.script.variable.def;

import io.lumine.mythic.lib.script.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.script.variable.VariableMetadata;
import io.lumine.mythic.lib.script.variable.VariableRegistry;

import java.util.Random;

@VariableMetadata(name = "random")
public class RandomVariable extends Variable<Random> {
    public static final SimpleVariableRegistry<Random> VARIABLE_REGISTRY = new SimpleVariableRegistry<>();
    public static final RandomVariable INSTANCE = new RandomVariable();

    private static final Random RANDOM = new Random();

    static {
        VARIABLE_REGISTRY.registerVariable("uniform", rand -> new DoubleVariable("temp", rand.nextDouble()), "unif", "double");
        VARIABLE_REGISTRY.registerVariable("gaussian", rand -> new DoubleVariable("temp", rand.nextGaussian()), "gauss");
        VARIABLE_REGISTRY.registerVariable("int", rand -> new IntegerVariable("temp", rand.nextInt()), "integer");
        VARIABLE_REGISTRY.registerVariable("bool", rand -> new BooleanVariable("temp", rand.nextBoolean()), "boolean");
    }

    private RandomVariable() {
        super("random", RANDOM);
    }

    @Override
    public VariableRegistry<Variable<Random>> getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }
}
