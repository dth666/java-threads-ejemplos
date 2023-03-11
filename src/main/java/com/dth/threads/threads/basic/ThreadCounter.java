package com.dth.threads.threads.basic;

import com.dth.threads.util.Counter;

/**
 *
 * @author dth666
 */
public class ThreadCounter extends Thread {

    private Counter c;
    private int syncType = 1;
    private int to = 10;

    public ThreadCounter(Counter c) {
        this.c = c;
    }

    public ThreadCounter(Counter c, int to, int syncType) {
        this.c = c;
        this.syncType = syncType;
        this.to = to;
    }

    @Override
    public void run() {
        if (syncType == 1) {
            count();
        } else if (syncType == 2) {
            countSymcMethod();
        } else if (syncType == 3) {
            countSync();
        }
    }

    private void count() {
        for (int i = 0; i < to; i++) {
            System.out.println(c.next());
        }
    }

    synchronized private void countSymcMethod() {
        for (int i = 0; i < to; i++) {
            System.out.println(c.next());
        }
    }

    private void countSync() {
        synchronized (c) {
            for (int i = 0; i < to; i++) {
                System.out.println(c.next());
            }
        }
    }
}
