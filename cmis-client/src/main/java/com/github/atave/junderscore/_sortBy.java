package com.github.atave.junderscore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Sorts a collection generated transforming a specified {@link Iterable}.
 *
 * @param <R> the type of a transformed value
 * @param <T> the type of a source value
 */
public abstract class _sortBy<R extends Comparable<R>, T> {

    protected abstract R process(T object);

    public Collection<R> on(Iterable<T> iterable) {
        List<R> retval = new ArrayList<>();
        for (T o : iterable) {
            retval.add(process(o));
        }
        Collections.sort(retval);
        return retval;
    }
}
