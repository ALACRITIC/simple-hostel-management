package org.remipassmoilesel.bookingsystem.utils.thread;


import org.remipassmoilesel.bookingsystem.systemtray.SystemTrayComponent;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for a task. allow to show the previous call stack for debugging.
 */
public class ManagedTask implements Runnable {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SystemTrayComponent.class);
    private static boolean debugMode = true;

    /**
     * Count all managed tasks launch
     */
    private static long count = 0;

    /**
     * Id of current task
     */
    private final long id;

    /**
     * Previous call stack of this managed task
     */
    private StackTraceElement[] callStack;

    /**
     * Task to run
     */
    private Runnable runnable;

    /**
     * If true, task is completed. It may end with normally, or with an exception, ...
     */
    private boolean done;

    public ManagedTask(Runnable run) {
        this.id = count++;
        runnable = run;
        callStack = Thread.currentThread().getStackTrace();

        done = false;
    }

    @Override
    public void run() {

        try {
            runnable.run();
        }

        // error happen, catch it
        catch (Throwable e) {

            StringBuilder text = new StringBuilder();
            text.append(ManagedTask.class.getSimpleName() + " #" + id + "/" + count + " - Previous calls: \n");
            for (StackTraceElement stackTraceElement : callStack) {
                text.append("\t" + stackTraceElement + "\n");
            }
            text.append("\n\n");

            logger.error("Caused by: ", e);
            logger.error(text.toString());

        }

        // finally mark it as done
        finally {
            done = true;
        }
    }

    public StackTraceElement[] getCallStack() {
        return callStack;
    }
}
