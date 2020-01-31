import com.jprofiler.api.probe.embedded.Split;
import internal.MockHelper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FactorialServer {

    private static final int THREAD_COUNT = 5;

    private static final Executor executor = Executors.newFixedThreadPool(THREAD_COUNT);

    public static Future<Long> requestFactorial(int number) {
        return CompletableFuture.supplyAsync(() -> supplyFactorial(number), executor);
    }

    private static long supplyFactorial(int number) {
        // Splits the call tree for each distinct value of the supplied string, so you can
        // analyze the sub-trees separately. You have to call a matching Split.exit().
        Split.enter(ThreadNameSplit.class, Thread.currentThread().getName());
        try {
            // You can nest splits on multiple levels
            Split.enter(FactorialServerProbe.class, "Factorial of " + number);
            try {
                return supplyFactorialWithoutSplit(number);
            } finally {
                // Completes the nested split. This should be called in a try-finally block to ensure that
                // it is executed in case of an exception.
                Split.exit();
            }
        } finally {
            // Completes the outer split.
            Split.exit();
        }
    }

    private static long supplyFactorialWithoutSplit(int number) {
        try (FactorialCacheConnection connection = FactorialCacheConnectionPool.getConnection()) {
            if (connection.isCached(number)) {
                return connection.get(number);
            } else {
                int factorial = calculateFactorial(number);
                connection.put(number, factorial);
                return factorial;
            }
        }
    }

    private static int calculateFactorial(int number) {
        // We artificially slow down the calculation, so the total recording time becomes macroscopic.
        // Probes should deal with macroscopic and not with microscopic events.
        MockHelper.work(80);
        if (number <= 1) {
            return 1;
        } else {
            return number * calculateFactorial(number - 1);
        }
    }
}
