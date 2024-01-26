package io.lumine.mythic.lib.script.variable;

import io.lumine.mythic.lib.skill.SkillMetadata;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// TODO inheritance eg Entity => Player
public class SimpleVariableRegistry<T extends Variable> implements VariableRegistry<T> {

    /**
     * A subvariable type is defined by its name and a function
     * which takes as input the variable and returns the corresponding subvariable.
     */
    private final Map<String, Function<T, Variable>> registered = new HashMap<>();

    /**
     * Called when retrieving a subvariable from a variable.
     * <p>
     * Throws a runtime exception if subvariable type cannot be found.
     *
     * @param t    Input variable
     * @param name Name of the subvariable
     * @return The corresponding subvariable
     */
    @NotNull
    public Variable accessVariable(@NotNull T t, @NotNull String name) {
        Function<T, Variable> supplier = registered.get(name);
        Validate.notNull(supplier, "Cannot find subvariable '" + name + "' in variable type '" + t.getClass().getAnnotation(VariableMetadata.class).name() + "'");
        return supplier.apply(t);
    }

    /**
     * @param name Subvariable name
     * @return If there exists a variable which such name
     */
    public boolean hasVariable(String name) {
        return registered.containsKey(name);
    }

    /**
     * Registers a subvariable for a specific variable
     *
     * @param name     Subvariable name
     * @param supplier Function that takes as input a variable with type given
     *                 as generic parameter which outputs the corresponding subvariable
     */
    public void registerVariable(@NotNull String name, @NotNull Function<T, Variable> supplier, String... aliases) {
        Validate.isTrue(!registered.containsKey(name), "A subvariable with the name '" + name + "' already exists");
        Validate.notNull(supplier, "Supplier cannot be null");
        Validate.isTrue(!SkillMetadata.RESERVED_VARIABLE_NAMES.contains(name), "Cannot use a reserved variable name");

        registered.put(name, supplier);

        for (String alias : aliases)
            registerVariable(alias, supplier);
    }
}
