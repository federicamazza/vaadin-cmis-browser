package com.github.atave.junderscore;

import org.junit.Assert;

public class _eachTest extends BaseTest<Integer> {

    @Override
    public void test() {
        setSource(1, 2, 3);

        final int[] count = {0};

        new _each<Integer>() {
            @Override
            protected void process(Integer object) {
                count[0] += 1;
            }
        }.on(getSource());

        Assert.assertEquals(count[0], getSource().size());
    }
}
