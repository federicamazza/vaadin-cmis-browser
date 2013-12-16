package com.github.atave.junderscore;

import org.junit.Assert;

public class _everyTest extends com.github.atave.junderscore.BaseTest<Integer> {
    @Override
    public void test() {
        setSource(1, 2, 3, 4, 5);

        // Test true
        boolean answer = new _every<Integer>() {
            @Override
            protected boolean test(Integer object) {
                return object < 6;
            }
        }.on(getSource());

        Assert.assertEquals(true, answer);

        // Test false
        answer = new _every<Integer>() {
            @Override
            protected boolean test(Integer object) {
                return object < 3;
            }
        }.on(getSource());

        Assert.assertEquals(false, answer);
    }
}
