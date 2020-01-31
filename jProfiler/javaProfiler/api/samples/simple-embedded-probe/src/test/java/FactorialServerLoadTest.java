import com.jprofiler.api.controller.Controller;
import org.junit.Test;

import java.io.File;
import java.util.Random;

public class FactorialServerLoadTest {

    @Test
    public void loadTest() {

        Controller.startCPURecording(true);
        Controller.startProbeRecording(Controller.PROBE_NAME_ALL_CUSTOM, true);

        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 1000; i++) {
            int number = random.nextInt(20);
            long factorial = FactorialServer.requestFactorial(number);
        }

        Controller.saveSnapshot(new File("factorial.jps"));
    }
}
