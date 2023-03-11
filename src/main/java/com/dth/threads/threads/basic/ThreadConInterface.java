package com.dth.threads.threads.basic;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dth666
 */
public class ThreadConInterface implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.print("i ");
            Math.cos((double) Math.random()*180);
        }
    }
    
}
