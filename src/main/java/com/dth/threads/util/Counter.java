package com.dth.threads.util;

/**
 *
 * @author dth666
 */
public class Counter {

    private int i = 0;

    public int next() {
        i = i + 1;
        return i;
    }

}
