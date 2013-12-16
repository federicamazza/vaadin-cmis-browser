package com.github.atave.junderscore;

import java.util.Comparator;

/**
 * Finds the minimum value in an {@link java.lang.Iterable}.
 *
 * @param <T> the type of that value
 */
public class _min<T> extends _max<T> {

    @Override
    protected boolean test(T o1, T o2, Comparator<T> comparator) {
        return !super.test(o1, o2, comparator);
    }

    @Override
    protected <T extends Comparable<T>> boolean testComparable(T o1, T o2, Comparator<T> comparator) {
        return !super.testComparable(o1, o2, comparator);
    }
}
