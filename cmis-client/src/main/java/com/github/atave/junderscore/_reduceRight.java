package com.github.atave.junderscore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The right-associative version of {@link com.github.atave.junderscore._reduce}.
 */
public abstract class _reduceRight<R, T> extends _reduce<R, T> {

    @Override
    public R on(Collection<T> collection, R memo) {
        List<T> list = new ArrayList<>(collection);
        Collections.reverse(list);
        return super.on(list, memo);
    }
}
