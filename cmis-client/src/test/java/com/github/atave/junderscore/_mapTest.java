package com.github.atave.junderscore;

import org.junit.Assert;

import java.util.Collection;

public class _mapTest extends BaseTest<String> {

    @Override
    public void test() {
        setSource("a", "aa", "aaa", "aaaa");

        Collection<Integer> sizes = new _map<Integer, String>() {
            @Override
            protected Integer process(String object) {
                return object.length();
            }
        }.on(getSource());

        Assert.assertEquals(sizes.size(), getSource().size());
    }
}
