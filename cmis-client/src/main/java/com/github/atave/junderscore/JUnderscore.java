package com.github.atave.junderscore;

import java.util.*;

public class JUnderscore {

    public static <T> _Collection<T> _(Collection<T> collection) {
        return new _Collection<T>(collection);
    }

}
