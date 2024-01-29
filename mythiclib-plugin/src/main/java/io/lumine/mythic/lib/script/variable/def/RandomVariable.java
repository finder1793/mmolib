package io.lumine.mythic.lib.script.variable.def;

import io.lumine.mythic.lib.script.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.script.variable.VariableMetadata;
import io.lumine.mythic.lib.script.variable.VariableRegistry;

import java.util.Random;

@VariableMetadata(name = "string")
public class RandomVariable extends Variable<Random> {
    public static final SimpleVariableRegistry<RandomVariable> VARIABLE_REGISTRY = new SimpleVariableRegistry<>();

    private static final Random RANDOM = new Random();

    public static final RandomVariable INSTANCE = new RandomVariable();

    static {
        VARIABLE_REGISTRY.registerVariable("uniform", rand -> new DoubleVariable("temp", rand.getStored().nextDouble()), "unif", "double");
        VARIABLE_REGISTRY.registerVariable("gaussian", rand -> new DoubleVariable("temp", rand.getStored().nextGaussian()), "gauss");
        VARIABLE_REGISTRY.registerVariable("int", rand -> new IntegerVariable("temp", rand.getStored().nextInt()), "integer");
        VARIABLE_REGISTRY.registerVariable("bool", rand -> new BooleanVariable("temp", rand.getStored().nextBoolean()), "boolean");
    }

    private RandomVariable() {
        super("random", RANDOM);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }
}
