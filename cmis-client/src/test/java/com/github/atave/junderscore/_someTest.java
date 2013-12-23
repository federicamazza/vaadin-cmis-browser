package com.github.atave.junderscore;

import org.junit.Assert;

public class _someTest extends BaseTest<Integer> {
    @Override
    public void test() {
        setSource(1, 2, 3, 4, 5);

        // Test false
        boolean answer = new _some<Integer>() {
            @Override
            protected boolean test(Integer object) {
                return object > 6;
            }
        }.on(getSource());

        Assert.assertEquals(false, answer);

        // Test true
        answer = new _some<Integer>() {
            @Override
            protected boolean test(Integer object) {
                return object == 2;
            }
        }.on(getSource());

        Assert.assertEquals(true, answer);
    }
}
