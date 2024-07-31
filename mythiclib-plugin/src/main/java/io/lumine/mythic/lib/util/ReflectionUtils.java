package io.lumine.mythic.lib.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class ReflectionUtils {

    @NotNull
    public static <T> Method getDeclaredMethod(@NotNull Class<T> clazz,
                                               @NotNull String methodName,
                                               @NotNull Class<?>... arguments) {
        try {
            return clazz.getDeclaredMethod(methodName, arguments);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @NotNull
    public static Object invoke(@NotNull Method method,
                                @NotNull Object caller,
                                @NotNull Object... arguments) {
        try {
            return method.invoke(caller, arguments);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
