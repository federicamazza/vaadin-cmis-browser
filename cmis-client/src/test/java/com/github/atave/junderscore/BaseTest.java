package com.github.atave.junderscore;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public abstract class BaseTest<T> {
    private List<T> source;

    protected List<T> getSource() {
        return source;
    }

    @SafeVarargs
    protected final void setSource(T... a) {
        source = Arrays.asList(a);
    }

    @Test
    public abstract void test();
}
