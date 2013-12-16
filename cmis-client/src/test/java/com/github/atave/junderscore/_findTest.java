package com.github.atave.junderscore;

import org.junit.Assert;

public class _findTest extends BaseTest<Integer> {

    @Override
    public void test() {
        setSource(1, 2, 3, 4, 5);

        int found = new _find<Integer>() {
            @Override
            protected boolean test(Integer object) {
                return object == 3;
            }
        }.on(getSource());

        Assert.assertEquals(3, found);
    }
}
