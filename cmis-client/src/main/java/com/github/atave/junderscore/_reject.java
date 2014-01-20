package com.github.atave.junderscore;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Finds all values in an {@link java.lang.Iterable} that do <u>not</u> match {@link #test}.
 *
 * @param <T> the type of those values
 */
public abstract class _reject<T> {

    protected abstract boolean test(T object);

    public Collection<T> on(Iterable<T> iterable) {
        Collection<T> retval = new ArrayList<>();
        for (T o : iterable) {
            if (!test(o)) {
                retval.add(o);
            }
        }
        return retval;
    }
}
