package com.github.atave.junderscore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Samples a {@link Collection}.
 */
public class _sample {

    private static final Random random = new Random();

    public static <T> Collection<T> on(Collection<T> collection, int n) {
        List<T> source = new ArrayList<>(collection);
        List<T> retval = new ArrayList<>();
        while (n-- > 0) {
            int i = random.nextInt(retval.size());
            retval.add(source.remove(i));
        }
        return retval;
    }

}
