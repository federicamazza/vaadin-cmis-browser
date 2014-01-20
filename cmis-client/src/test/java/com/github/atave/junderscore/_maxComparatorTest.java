package com.github.atave.junderscore;

import org.junit.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class _maxComparatorTest extends BaseTest<Collection> {
    @Override
    public void test() {
        setSource(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );

        Comparator<Collection> comparator = new Comparator<Collection>() {
            @Override
            public int compare(Collection o1, Collection o2) {
                return o1.size() - o2.size();
            }
        };

        Collection max = new _max<Collection>().on(getSource(), comparator);

        Assert.assertEquals(max.size(), 3);
    }
}
