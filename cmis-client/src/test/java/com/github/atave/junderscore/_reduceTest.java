package com.github.atave.junderscore;

import org.junit.Assert;

public class _reduceTest extends BaseTest<Integer> {

    @Override
    public void test() {
        setSource(1, 1, 1, 1, 1, 1, 1);

        int value = new _reduce<Integer, Integer>() {
            @Override
            protected Integer process(Integer memo, Integer object) {
                return memo + object;
            }
        }.on(getSource(), 0);

        Assert.assertEquals(getSource().size(), value);
    }
}
