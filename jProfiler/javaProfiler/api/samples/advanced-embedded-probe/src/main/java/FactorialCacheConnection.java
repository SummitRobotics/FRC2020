import com.jprofiler.api.probe.embedded.Payload;

import java.util.concurrent.atomic.AtomicInteger;

public class FactorialCacheConnection implements AutoCloseable {

    private static AtomicInteger counter = new AtomicInteger();

    public FactorialCacheConnection() {
        // Payload events can be grouped by associated long-lived objects, called "control objects".
        // Usually, control objects are connections to an external resource.
        // Control objects should be opened explicitly, but they can also by supplied dynamically to Payload.enter(),
        // in which case FactorialCacheProbe.getControlObjectName will be queried for the name.
        Payload.openControlObject(FactorialCacheProbe.class, this, "Factorial cache connection #" + counter.incrementAndGet());
    }

    public boolean isCached(int number) {
        // A call to Payload.enter() will start a payload annotation into the call tree. It must be matched
        // with a corresponding call to Payload.exit(). This version of the enter method takes a control
        // object and a custom type. They are optional and you can write payload probes without them.
        // The control object in this case is the connection.
        Payload.enter(FactorialCacheProbe.class, this, CacheOperationType.READ);
        try {
            return FactorialCache.isCached(number);
        } finally {
            // In Payload.exit(), you supply the description of the payload that will be shown in the
            // probe. Make sure to use a try-finally block for this construct to avoid missing calls.
            Payload.exit("Check for cached calculation");
        }
    }

    public long get(int number) {
        Payload.enter(FactorialCacheProbe.class, this, CacheOperationType.READ);
        try {
            return FactorialCache.get(number);
        } finally {
            Payload.exit("Get cached calculation");
        }
    }

    public void put(int number, long factorial) {
        // Instead of the enter/exit pair, Payload (as well as Split) offer "execute" methods with Runnable
        // or Callable parameters. With Java lambdas, the invocation looks nicer, but is not suitable for
        // primitive return types or methods that throw checked exceptions.
        Payload.execute(FactorialCacheProbe.class, "Cache calculation for factorial of " + number,
                this, CacheOperationType.WRITE, () -> FactorialCache.put(number, factorial));
    }

    @Override
    public void close() {
        FactorialCacheConnectionPool.returnConnection(this);
    }
}
