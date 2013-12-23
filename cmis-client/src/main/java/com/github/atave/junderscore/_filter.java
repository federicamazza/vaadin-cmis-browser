package com.github.atave.junderscore;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Finds all values in an {@link Iterable} matching {@link #test}.
 *
 * @param <T> the type of those values
 */
public abstract class _filter<T> {

    protected abstract boolean test(T object);

    public Collection<T> on(Iterable<T> iterable) {
        Collection<T> retval = new ArrayList<>();
        for (T o : iterable) {
            if (test(o)) {
                retval.add(o);
            }
        }
        return retval;
    }
}
