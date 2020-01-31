package internal;

import com.jprofiler.api.probe.embedded.ThreadState;

public class MockHelper {

    public static void work(int ms) {
        // We ask the profiling agent to record this method in the "Runnable" thread state, even though it is actually sleeping
        ThreadState.RUNNABLE.enter();
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // It is important to reset the thread state at the end of the method
            ThreadState.exit();
        }
    }
}
