package com.github.atave.junderscore;

import java.util.HashMap;
import java.util.Map;

/**
 * Like {@link com.github.atave.junderscore._groupBy}, but with unique keys.
 */
public abstract class _indexBy<K, T> {

    protected abstract K process(T object);

    public Map<K, T> on(Iterable<T> iterable) {
        Map<K, T> map = new HashMap<>();

        for (T o : iterable) {
            map.put(process(o), o);
        }

        return map;
    }
}
