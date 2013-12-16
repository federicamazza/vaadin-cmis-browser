package com.github.atave.junderscore;

import org.junit.Assert;

import java.util.Map;

public class _countByTest extends com.github.atave.junderscore.BaseTest<Integer> {
    @Override
    public void test() {
        setSource(1, 2, 3, 4, 5, 6, 7);

        Map<Integer, Integer> groupCounts = new _countBy<Integer, Integer>() {
            @Override
            protected Integer process(Integer object) {
                return object % 2;
            }
        }.on(getSource());

        Assert.assertEquals(groupCounts.get(0).intValue(), 3);
        Assert.assertEquals(groupCounts.get(1).intValue(), 4);
    }
}
