import com.jprofiler.api.probe.embedded.Payload;
import internal.MockHelper;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FactorialCache {

    private static HashMap<Integer, Long> cache = new HashMap<>();

    public static boolean isCached(int number) {
        // A call to Payload.enter() will start a payload annotation into the call tree. It must be matched
        // with a corresponding call to Payload.exit().
        Payload.enter(FactorialCacheProbe.class);
        try {
            MockHelper.work(2);
            return cache.containsKey(number);
        } finally {
            // In Payload.exit(), you supply the description of the payload that will be shown in the
            // probe. Make sure to use a try-finally block for this construct to avoid missing calls.
            Payload.exit("Check for cached calculation");
        }
    }

    public static long get(int number) {
        Payload.enter(FactorialCacheProbe.class);
        try {
            MockHelper.work(5);
            return cache.get(number);
        } finally {
            Payload.exit("Get cached calculation");
        }
    }

    public static void put(int number, long factorial) {
        // Puts are counted for the extra telemetry defined in FactorialCacheProbe.
        putCount.incrementAndGet();
        Payload.execute(FactorialCacheProbe.class, "Cache calculation for factorial of " + number, () -> {
            MockHelper.work(100);
            cache.put(number, factorial);
        });
    }

    // Counter for the telemetry. The number classes from the java.util.concurrent.atomic package
    // are a thread-safe way to maintain such counters
    private static AtomicInteger putCount = new AtomicInteger();

    public static int getPutCount() {
        // When this method is called, a new interval is started, so the counter is returned and reset at the same time.
        return putCount.getAndSet(0);
    }
}
