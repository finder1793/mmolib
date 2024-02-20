package io.lumine.mythic.lib.util;

/**
 * A function that can throw an exception.
 */
@FunctionalInterface
public interface ThrowingFunction<T, U> {
    U accept(T t) throws Exception;
}
