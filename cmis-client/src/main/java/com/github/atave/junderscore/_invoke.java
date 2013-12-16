package com.github.atave.junderscore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * Invokes a method on every passed object.
 */
public class _invoke {
    public static <T> void on(Iterable<T> iterable, final String methodName, final Object... args) throws InvocationTargetException, IllegalAccessException {
        for (T o : iterable) {
            new _find<Method>() {
                @Override
                protected boolean test(Method object) {
                    return object.getName().equals(methodName) && object.getParameterTypes().length == args.length;
                }
            }.on(Arrays.asList(o.getClass().getMethods())).invoke(o, args);
        }
    }
}
