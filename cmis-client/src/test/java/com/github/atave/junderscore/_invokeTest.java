package com.github.atave.junderscore;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.fail;

public class _invokeTest extends BaseTest<String> {
    @Override
    public void test() {
        setSource("a", "aa", "aaa");

        try {
            _invoke.on(getSource(), "length");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }
}
