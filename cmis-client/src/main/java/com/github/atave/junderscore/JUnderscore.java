package com.github.atave.junderscore;

import java.util.Collection;

public class JUnderscore {

    public static <T> _Collection<T> _(Collection<T> collection) {
        return new _Collection<>(collection);
    }

}
