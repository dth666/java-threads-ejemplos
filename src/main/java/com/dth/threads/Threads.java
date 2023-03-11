package com.dth.threads;

import com.dth.threads.prodcons.ThreadSync;
import com.dth.threads.threads.basic.ThreadConHerencia;
import com.dth.threads.threads.basic.ThreadConInterface;
import com.dth.threads.threads.basic.ThreadCounter;
import com.dth.threads.util.Counter;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dth666
 */
public class Threads {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Threads t = new Threads();
        //t.simpleExec();
        //t.threadWithoutClass();
        //t.multipleExec();
        //t.multipleExecConJoin();
        //t.execNice();
        //t.counterRace();
        //t.threadPool(10);
        //t.waitNotify();
        //t.futuresLv1();
        //t.futuresLv2();
        t.futuresLv2C1();
    }

    /**
     * Creación y ejecución básica de hilos
     */
    private void simpleExec() {
        Thread h1 = new ThreadConHerencia();
        h1.start();
        /*Thread h2 = new Thread(new ThreadConInterface());
        h2.start();*/
    }

    /**
     * Como crear un hilo sobre la marcha para tareas simples o monitoreo de
     * algún proceso en ejecucións. Es simplemete otra forma de definir un
     * thread.
     *
     */
    private void threadWithoutClass() {
        String algo = "Cosas verdes";
        Runnable satyr = () -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(500);
                    System.out.println(algo + " " + i);
                } catch (InterruptedException ex) {
                }
            }
        };

        new Thread(satyr).start();

    }

    /**
     * Ejecución de varios hilos al tiempo
     */
    private void multipleExec() {
        Thread h1 = new ThreadConHerencia();
        Thread h2 = new Thread(new ThreadConInterface());
        h1.start();
        h2.start();
        System.out.println("");
        System.out.println("SOY LA ÚLTIMA LÍNEA DE EJECUCIÓN");
    }

    /**
     * Ejecución de varios hilos al tiempo, esperando la ejecución de uno antes
     * de continuar.
     *
     * @throws InterruptedException
     */
    private void multipleExecConJoin() throws InterruptedException {
        Thread h1 = new ThreadConHerencia();
        Thread h2 = new Thread(new ThreadConInterface());
        h1.start();
        h2.start();
        h1.join();
        System.out.println("");
        System.out.println("SOY LA ÚLTIMA LÍNEA DE EJECUCIÓN");
    }

    /**
     * Una forma de dar pistas al scheduler para que le de el control a otro
     * hilo.
     */
    private void execNice() {
        Thread h1 = new Thread(new ThreadSync("H1"));
        Thread h2 = new Thread(new ThreadSync("H2"));
        h1.start();
        h2.start();
    }

    /**
     * Sync de hilos, como funciona synchronized, que es un monitor. Ejemplo de
     * como hacer sync el contador y opciones
     */
    private void counterRace() {
        Counter c = new Counter();
        int syncType = 1; //1: NoSync, 2: SyncMethod, 3: SyncExternalMonitor
        ThreadCounter tc1 = new ThreadCounter(c, 10, syncType);
        ThreadCounter tc2 = new ThreadCounter(c, 10, syncType);

        tc1.start();
        tc2.start();
    }

    /**
     * Como usar unn threadpool por que usarlo, como finalizarlo, que pasa si no
     * se finaliza, como hacerlo "infinito"
     *
     * @param threads
     */
    private void threadPool(int threads) {
        Counter c = new Counter();
        int syncType = 1; //1: NoSync, 2: SyncMethod, 3: SyncExternalMonitor
        ExecutorService es = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            es.execute(new ThreadCounter(c, 3, syncType));
        }
        es.shutdown();
    }

    /**
     * Como sincronizar hilos, por que hay que poner sleep, riesgos ...
     */
    private void waitNotify() {
        LinkedList<String> l = new LinkedList<>();

        Thread hilo1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {

                int sleepSecs = (int) ((Math.abs(Math.random() * 3) * 1000) + 2000) / 1000 * 1000;
                try {
                    synchronized (l) {
                        System.out.println("Me tardaré " + sleepSecs + " en producir algo...");
                        Thread.sleep(sleepSecs);
                        l.add("Producto que tardó " + sleepSecs + " en producirse");
                        l.notify();
                    }
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Threads.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        Thread hilo2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                synchronized (l) {
                    while (l.isEmpty()) {
                        try {
                            System.out.println("Esperando ...");
                            l.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Threads.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("IMPR: " + l.pop());
                    System.out.println("");
                }
            }
        });

        hilo2.start();
        hilo1.start();
    }

    /**
     * Formas "modernas" de realizar tareas en paralelo, Nivel 1
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void futuresLv1() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        System.out.println("Ejecutando tarea A que tarda 10 segundos");
        Future<String> future1 = executorService.submit(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(" Tarea A (" + i + "s)");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return "Finalizada tarea A\n\n";
        });

        Thread.sleep(3500);
        System.out.println("Ejecutando tarea B que tarda 10 segundos");
        Future<String> future2 = executorService.submit(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(" Tarea B (" + i + "s)");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return "Finalizada tarea B\n\n";
        });

        executorService.shutdown();
//Long result = future.get(10, TimeUnit.SECONDS);
        System.out.println("Esperando tarea A");
        String result = future1.get();
        System.out.println(result);

        System.out.println("Esperando tarea B");
        String result1 = future2.get();
        System.out.println(result1);
    }

    /**
     * Forma moderna de hacer tareas en paralelo nivel 2, encadenamiento, que es
     * un ForkJoin pool.
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void futuresLv2() throws InterruptedException, ExecutionException {

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {

                }
                System.out.println(" * ");
            }
            return "Hello";
        });

//Encadenar un método para procesar
        CompletableFuture<String> future2 = completableFuture.thenApply(s -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {

                }
                System.out.println(" + ");
            }
            return s + " World";
        });

        //CompletableFuture<String> future3 = future2.thenApply(s -> {for (int i = 0; i < 5; i++) {try {Thread.sleep(500);} catch (InterruptedException ex) {}System.out.println(" + ");}return s + " of shit";});
        System.out.println(future2.get());
    }

    /**
     * Forma oderna de hacer tareas en paralelo, muchas tareas al tiempo, pero necesito que todas finalicen antes de poder continuar con otras tareas.
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void futuresLv2C1() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Threads.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Fin Hello");
            return "Hello ";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Threads.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Fin fucking");
            return "fucking ";
        });
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Threads.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Fin World");
            System.out.println("\n======\n");
            return "World ";

        });
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);
        combinedFuture.get();
        System.out.println(future1.get() + future2.get() + future3.get());

        System.out.println("Se han terminado todas las tareas!");

        System.out.println(future1.isDone());
        System.out.println(future2.isDone());
        System.out.println(future3.isDone());
    }

}
