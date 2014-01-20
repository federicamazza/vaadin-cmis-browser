package com.github.atave.junderscore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Generates a shuffled copy of a {@link Collection}.
 */
public class _shuffle {

    public static <T> Collection<T> on(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        Collections.shuffle(list);
        return list;
    }
}
