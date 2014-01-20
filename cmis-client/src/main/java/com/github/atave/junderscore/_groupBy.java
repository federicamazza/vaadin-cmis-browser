package com.github.atave.junderscore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Splits an {@link Iterable} in sets, grouped by the result of
 * running each value through {@link #process}.
 *
 * @param <K> the key type
 * @param <T> the value type
 */
public abstract class _groupBy<K, T> {

    protected abstract K process(T object);

    public Map<K, Collection<T>> on(Iterable<T> iterable) {
        Map<K, Collection<T>> map = new HashMap<>();

        for (T o : iterable) {
            K key = process(o);
            Collection<T> values = map.get(key);

            if (values == null) {
                values = new ArrayList<>();
                map.put(key, values);
            }

            values.add(o);
        }

        return map;
    }
}
