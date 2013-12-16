package com.github.atave.junderscore;

import junit.framework.Assert;

public class _maxTest extends com.github.atave.junderscore.BaseTest<Integer> {
    @Override
    public void test() {
        setSource(1, 2, 3);

        int max = new _max<Integer>().on(getSource());

        Assert.assertEquals(max, 3);
    }
}
