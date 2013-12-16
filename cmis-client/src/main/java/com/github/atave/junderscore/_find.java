package com.github.atave.junderscore;

/**
 * Finds the first value in an {@link Iterable} matching {@link #test}.
 *
 * @param <T> the type of the value
 */
public abstract class _find<T> {

    protected abstract boolean test(T object);

    public T on(Iterable<T> iterable) {
        for (T o : iterable) {
            if (test(o)) {
                return o;
            }
        }
        return null;
    }
}
