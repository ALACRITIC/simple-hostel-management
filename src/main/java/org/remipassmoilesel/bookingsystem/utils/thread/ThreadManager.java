package org.remipassmoilesel.bookingsystem.utils.thread;

import org.remipassmoilesel.bookingsystem.systemtray.SystemTrayComponent;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BooleanSupplier;

/**
 * Utility to run tasks in others threads.
 */
public class ThreadManager {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SystemTrayComponent.class);

    private static long executorTaskCount = 0;

    /**
     * Thread pool
     */
    private static ThreadPoolExecutor executor;

    /**
     * Run a task in a separated thread
     *
     * @param run
     */
    public static ManagedTask runLater(Runnable run) {
        return runLater(run, false);
    }

    /**
     * Wait specified time before run task. Task may be runned little after waiting time.
     *
     * @param run
     * @param timeMilliSec
     */
    public static ManagedTask runLater(Runnable run, int timeMilliSec) {
        return runLater(run, false, timeMilliSec);
    }

    /**
     * Wait specified time before run task. Task may be runned little after waiting time.
     *
     * @param timeMilliSec
     * @param run
     */
    public static ManagedTask runLater(Runnable run, boolean onEdt, int timeMilliSec) {

        ManagedTask task = new ManagedTask(run);

        ThreadManager.runLater(() -> {
            try {
                Thread.sleep(timeMilliSec);
            } catch (InterruptedException e) {
                logger.error("Error while executing: " + run, e);
            }

            runLater(task, onEdt);
        });

        return task;
    }

    /**
     * Execute a task in a separated thread, eventually on EDT
     *
     * @param runnable
     */
    public static ManagedTask runLater(Runnable runnable, boolean onEDT) {

        ManagedTask task = null;
        if (runnable instanceof ManagedTask) {
            task = (ManagedTask) runnable;
        } else {
            task = new ManagedTask(runnable);
        }

        if (onEDT) {
            SwingUtilities.invokeLater(task);
        } else {
            getExecutor().execute(task);
        }

        return task;
    }

    /**
     * Create thread pool if necessary and return it.
     * Pool size will be determined by number of processors
     * but never below 2
     *
     * @return
     */
    private static ExecutorService getExecutor() {

        if (executor == null) {
            // keep a minimum of 4 threads, to prevent error on event dispatch
            int minPoolSize = 4;
            int nbProcs = Runtime.getRuntime().availableProcessors();
            int threadPoolSize = nbProcs * 2;
            threadPoolSize = threadPoolSize >= minPoolSize ? threadPoolSize : minPoolSize;
            executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    executorTaskCount++;
                    Thread t = new Thread(r);
                    t.setName("Abm_thread-" + executorTaskCount);
                    return t;
                }
            });

            // set minimum thread always up
            //executor.setCorePoolSize(threadPoolSize);

            //System.out.println("Initializing thread pool: " + nbProcs + " processors, threads: " + nbThreads);
        }

        return executor;
    }

    /**
     * Shutdown thread manager
     */
    public static void shutDown() {

        // stop gently
        getExecutor().shutdown();
    }

    /**
     * Allow to run method at specified interval on EDT
     * <p>
     * Inside boolean supplier, return false to stop timer
     *
     * @param interval
     * @param run
     */
    public static void runTimerOnEDT(int interval, BooleanSupplier run) {

        ActionListener action = (e) -> {

            if (SwingUtilities.isEventDispatchThread() == false) {
                throw new IllegalStateException("Do not run this method on Event Dispatch Thread");
            }

            // perform action and get return value
            boolean continueTimer = run.getAsBoolean();

            // if return false, stop timer
            if (continueTimer == false) {
                ((Timer) e.getSource()).stop();
            }
        };

        // launch timer
        Timer timer = new Timer(interval, action);
        timer.start();
    }
}
