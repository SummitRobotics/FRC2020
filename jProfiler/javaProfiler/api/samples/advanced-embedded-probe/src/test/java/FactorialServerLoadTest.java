import com.jprofiler.api.controller.Controller;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FactorialServerLoadTest {

    @Test
    public void loadTest() {

        Controller.startCPURecording(true);
        Controller.startProbeRecording(Controller.PROBE_NAME_ALL_CUSTOM, true);

        List<Future<Long>> results = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 2000; i++) {
            int number = random.nextInt(20);
            results.add(FactorialServer.requestFactorial(number));
        }

        for (Future<Long> result : results) {
            try {
                result.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        Controller.saveSnapshot(new File("factorial.jps"));
    }
}
