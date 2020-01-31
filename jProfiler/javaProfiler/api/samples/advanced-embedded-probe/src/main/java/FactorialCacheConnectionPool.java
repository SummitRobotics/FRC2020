import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class FactorialCacheConnectionPool {
    private static ConcurrentLinkedQueue<FactorialCacheConnection> queue = new ConcurrentLinkedQueue<>();

    public static FactorialCacheConnection getConnection() {
        FactorialCacheConnection connection = queue.poll();
        if (connection == null) {
            connection = new FactorialCacheConnection();
        }
        return connection;
    }

    public static void returnConnection(FactorialCacheConnection object) {
        queue.offer(object);
    }
}