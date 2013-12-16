package com.github.atave.junderscore;

/**
 * Iterates over an {@link Iterable}, yielding each value in turn to a function.
 *
 * @param <T> the type of each value
 */
public abstract class _each<T> {

    protected abstract void process(T object);

    public void on(Iterable<T> iterable) {
        for (T o : iterable) {
            process(o);
        }
    }
}
