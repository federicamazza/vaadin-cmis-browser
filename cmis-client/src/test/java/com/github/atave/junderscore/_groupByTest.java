package com.github.atave.junderscore;

import org.junit.Assert;

import java.util.Collection;
import java.util.Map;

public class _groupByTest extends com.github.atave.junderscore.BaseTest<String> {
    @Override
    public void test() {
        setSource("a", "aaa", "aaa", "a", "aa");

        Map<Integer, Collection<String>> groups = new _groupBy<Integer, String>() {
            @Override
            protected Integer process(String object) {
                return object.length();
            }
        }.on(getSource());

        Assert.assertEquals(groups.get(1).size(), 2);
        Assert.assertEquals(groups.get(3).size(), 2);
        Assert.assertEquals(groups.get(2).size(), 1);
    }
}
