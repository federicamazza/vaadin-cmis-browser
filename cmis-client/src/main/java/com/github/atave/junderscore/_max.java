package com.github.atave.junderscore;

import java.util.Comparator;

/**
 * Finds the maximum value in an {@link Iterable}.
 *
 * @param <T> the type of the value
 */
public class _max<T> {

    protected boolean test(T o1, T o2, Comparator<T> comparator) {
        return comparator.compare(o1, o2) < 0;
    }

    protected <T extends Comparable<T>> boolean testComparable(T o1, T o2, Comparator<T> comparator) {
        return comparator.compare(o1, o2) < 0;
    }

    public T on(Iterable<T> iterable, Comparator<T> comparator) {
        T max = null;
        for (T o : iterable) {
            if (max == null) {
                max = o;
            } else {
                if (test(max, o, comparator)) {
                    max = o;
                }
            }
        }
        return max;
    }

    public <T extends Comparable<T>> T on(Iterable<T> iterable) {
        Comparator<T> comparator = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.compareTo(o2);
            }
        };

        T max = null;
        for (T o : iterable) {
            if (max == null) {
                max = o;
            } else {
                if (testComparable(max, o, comparator)) {
                    max = o;
                }
            }
        }
        return max;
    }
}
