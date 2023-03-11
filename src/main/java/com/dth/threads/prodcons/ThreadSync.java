package com.dth.threads.prodcons;

/**
 *
 * @author dth666
 */
public class ThreadSync implements Runnable {

    private final String name;

    public ThreadSync(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            System.out.println(name + ": " + i);
            Thread.yield();
        }
    }

}
