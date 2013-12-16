package com.github.atave.junderscore;

/**
 * Returns whether any of the values passes {@link #test}.
 *
 * @param <T> the type of those values
 */
public abstract class _some<T> {

    protected abstract boolean test(T object);

    public boolean on(Iterable<T> iterable) {
        for (T o : iterable) {
            if (test(o)) {
                return true;
            }
        }
        return false;
    }
}
