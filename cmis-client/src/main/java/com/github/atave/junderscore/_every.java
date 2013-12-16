package com.github.atave.junderscore;

/**
 * Returns whether or not all values in an {@link Iterable} match {@link #test}.
 *
 * @param <T> the type of those values
 */
public abstract class _every<T> {

    protected abstract boolean test(T object);

    public boolean on(Iterable<T> iterable) {
        for (T o : iterable) {
            if (!test(o)) {
                return false;
            }
        }
        return true;
    }
}
