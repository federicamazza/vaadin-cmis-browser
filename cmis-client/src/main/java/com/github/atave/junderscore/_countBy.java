package com.github.atave.junderscore;

import java.util.HashMap;
import java.util.Map;

/**
 * Groups an {@link Iterable} and returns the number of objects for each group.
 *
 * @param <K> the key type
 * @param <T> the value type
 */
public abstract class _countBy<K, T> {

    protected abstract K process(T object);

    public Map<K, Integer> on(Iterable<T> iterable) {
        Map<K, Integer> map = new HashMap<>();

        for (T o : iterable) {
            K key = process(o);
            Integer values = map.get(key);

            if (values == null) {
                values = 0;
            }

            map.put(key, values + 1);
        }

        return map;
    }

}
