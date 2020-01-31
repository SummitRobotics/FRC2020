import com.jprofiler.api.controller.Controller;
import com.jprofiler.api.controller.HeapDumpOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// This class shows how to use the offline profiling API in the Controller class.
// Please see the "Offline profiling" topic in the manual for a systematic discussion of this feature.
public class TestProgram {

    private static final int COUNT = 100000;

    // These lists hold objects to illustrate memory profiling
    private static List<Double> sines = new ArrayList<>(COUNT);
    private static List<Double> squareRoots = new ArrayList<>(COUNT);
    private static List<Double> logs = new ArrayList<>(COUNT);

    private static boolean triggerHeapDumps = false;

    public static void main(String[] args) {

        // Pass "true" as the first parameter to trigger heap dumps
        if (args.length > 0 && Boolean.parseBoolean(args[0])) {
            triggerHeapDumps = true;
        }

        // On startup, JProfiler does not record any data. The various recording subsystems have to be
        // switched on programatically.
        Controller.startCPURecording(true);
        Controller.startAllocRecording(true);
        Controller.startThreadProfiling();
        Controller.startVMTelemetryRecording();

        // This is observer method
        calculateStuff();

        // You can switch off recording at any point. Recording can be switched on again.
        Controller.stopCPURecording();
        Controller.stopAllocRecording();
        Controller.stopThreadProfiling();
        Controller.stopVMTelemetryRecording();
    }

    private static void calculateStuff() {

        // Bookmarks can be added with the API.
        Controller.addBookmark("Start calculating sines");
        calculateSines();
        if (triggerHeapDumps) {
            // If you would like to use the heap walker for saved snapshots, you have to trigger a heap dump at some point.
            // The last heap dump will be saved in the snapshot file. This makes snapshot files much larger and creates
            // significant memory overhead.
            Controller.triggerHeapDump(HeapDumpOptions.SELECT_RECORDED);
        }
        // Now we save a snapshot with all recorded profiling data
        Controller.saveSnapshot(new File("after_sines.jps"));

        // The same sequence with a different method
        Controller.addBookmark("Start calculating square roots");
        calculateSquareRoots();
        if (triggerHeapDumps) {
            Controller.triggerHeapDump(HeapDumpOptions.SELECT_RECORDED);
        }
        Controller.saveSnapshot(new File("after_square_roots.jps"));

        // And a third time
        Controller.addBookmark("Start calculating logs");
        calculateLogs();
        if (triggerHeapDumps) {
            Controller.triggerHeapDump(HeapDumpOptions.SELECT_RECORDED);
        }
        Controller.saveSnapshot(new File("after_logs.jps"));
    }

    private static void calculateSines() {
        double increment = (Math.PI / 2) / COUNT;
        for (int i = 0; i < COUNT; i++) {
            sines.add(Math.sin(increment * i));
        }
    }

    private static void calculateSquareRoots() {
        for (int i = 0; i < COUNT; i++) {
            squareRoots.add(Math.sqrt(i));
        }
    }

    private static void calculateLogs() {
        for (int i = 0; i < COUNT; i++) {
            logs.add(Math.log(i + 1));
        }
    }
}
