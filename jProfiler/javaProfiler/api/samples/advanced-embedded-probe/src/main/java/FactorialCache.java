import internal.MockHelper;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FactorialCache {

    private static HashMap<Integer, Long> cache = new HashMap<>();

    public static boolean isCached(int number) {
        // Count the various event types
        checkCount.incrementAndGet();
        MockHelper.work(5);
        return cache.containsKey(number);
    }

    public static long get(int number) {
        getCount.incrementAndGet();
        MockHelper.work(15);
        return cache.get(number);
    }

    public static void put(int number, long factorial) {
        putCount.incrementAndGet();
        MockHelper.work(250);
        cache.put(number, factorial);
    }

    // Counters for the telemetry. The number classes from the java.util.concurrent.atomic package
    // are a thread-safe way to maintain such counters
    private static AtomicInteger getCount = new AtomicInteger();
    private static AtomicInteger checkCount = new AtomicInteger();
    private static AtomicInteger putCount = new AtomicInteger();

    public static int getGetCount() {
        return getCount.getAndSet(0);
    }

    public static int getCheckCount() {
        return checkCount.getAndSet(0);
    }

    public static int getPutCount() {
        return putCount.getAndSet(0);
    }
}
