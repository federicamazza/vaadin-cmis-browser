package com.github.atave.junderscore;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Produces a new collection by transforming each element of a source collection.
 *
 * @param <R> the type of a transformed element
 * @param <T> the type of a source element
 */
public abstract class _map<R, T> {

    protected abstract R process(T object);

    public Collection<R> on(Iterable<T> iterable) {
        Collection<R> retval = new ArrayList<>();
        for (T o : iterable) {
            retval.add(process(o));
        }
        return retval;
    }
}
