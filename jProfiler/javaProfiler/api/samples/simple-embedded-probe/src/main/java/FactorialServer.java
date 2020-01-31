import com.jprofiler.api.probe.embedded.Split;
import internal.MockHelper;

public class FactorialServer {

    public static long requestFactorial(int number) {
        // Splits the call tree for each distinct value of the supplied string, so you can
        // analyze the sub-trees separately. You have to call a matching Split.exit().
        Split.enter(FactorialServerProbe.class, "Factorial of " + number);
        try {
            if (FactorialCache.isCached(number)) {
                return FactorialCache.get(number);
            } else {
                int factorial = calculateFactorial(number);
                FactorialCache.put(number, factorial);
                return factorial;
            }
        } finally {
            // Completes the above split. This should be called in a try-finally block to ensure that
            // it is executed in case of an exception.
            Split.exit();
        }
    }

    private static int calculateFactorial(int number) {
        // We artificially slow down the calculation, so the total recording time becomes macroscopic.
        // Probes should deal with macroscopic and not with microscopic events.
        MockHelper.work(30);
        if (number <= 1) {
            return 1;
        } else {
            return number * calculateFactorial(number - 1);
        }
    }
}
