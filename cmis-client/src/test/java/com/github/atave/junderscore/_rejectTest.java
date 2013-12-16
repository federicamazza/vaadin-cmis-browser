package com.github.atave.junderscore;

import org.junit.Assert;

import java.util.Collection;

public class _rejectTest extends BaseTest<Integer> {
    @Override
    public void test() {
        setSource(1, 2, 3, 4, 5);

        Collection<Integer> filtered = new _reject<Integer>() {
            @Override
            protected boolean test(Integer object) {
                return object % 2 == 0;
            }
        }.on(getSource());

        Assert.assertEquals(filtered.size(), 3);
    }
}
