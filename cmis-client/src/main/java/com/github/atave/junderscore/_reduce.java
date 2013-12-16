package com.github.atave.junderscore;

import java.util.Collection;

/**
 * Transforms a {@link Collection} into a single value.
 *
 * @param <R> the type of the single value
 * @param <T> the type of the original values
 */
public abstract class _reduce<R, T> {

    protected abstract R process(R memo, T object);

    public R on(Collection<T> collection, R memo) {
        for (T o : collection) {
            memo = process(memo, o);
        }
        return memo;
    }
}
