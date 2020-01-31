import com.jprofiler.api.platform.connection.Connection;
import com.jprofiler.api.platform.connection.ConnectionFactory;
import com.jprofiler.api.platform.data.CpuHotspots;
import com.jprofiler.api.platform.descriptors.Descriptor;
import com.jprofiler.api.platform.parameters.Aggregation;
import com.jprofiler.api.platform.parameters.ThreadStatus;
import com.jprofiler.api.platform.structures.HeapValue;
import com.jprofiler.api.platform.structures.ProfilingValue;
import com.jprofiler.api.platform.structures.ThreadInfo;
import com.jprofiler.api.platform.structures.Tree;
import com.jprofiler.api.platform.util.TreePrinter;

import java.io.IOException;
import java.util.*;

// This class shows how to build a custom profiler.
// This profiler observes the execution of Java code in the same JVM and extracts some profiling data from the
// profiling agent. For a complete list of available operations, please see the Javadoc for the JProfiler platform.
//
// To compile and start this program, use the build.xml file in the same directory.
//
public class TestProfiler {

    // The Connection class is the bridge to the native JProfiler agent.
    private static Connection connection;

    public static void main(String[] args) throws IOException {

        // Here, we open a connection to the JVM we're currently running in. We
        // could also connect to remote JVMs and to saved snapshots.
        connection = ConnectionFactory.getLocalConnection();

        // start recording CPU data
        connection.recordCpuData(true);
        // start recording custom probe data
        connection.recordPayload(true, LoadFactorProbe.class.getName());

        // execute the observed "program"
        new LoadFactorTest().execute();
        // the observed program has finished

        // stop recording CPU data so we don't get interferences with the analysis code
        connection.recordCpuData(false);
        // print profiling data to console
        dumpProfilingData();

        connection.close();
    }

    private static void dumpProfilingData() {

        System.err.println("Profiling data :");
        dumpMemoryData();

        // Look for a specific thread. In this case, this would not be really necessary since all
        // work was done on the main thread and we could use the data cumulated for all threads.
        ThreadInfo mainThread = null;
        for (ThreadInfo threadInfo : connection.getThreadInfos()) {
            if (threadInfo.getThreadName().equals("main")) {
                mainThread = threadInfo;
                break;
            }
        }

        if (mainThread != null) {
            dumpHotSpots(mainThread);
        }

        dumpCallTree();
        dumpPayloadHotSpots();
    }

    private static void dumpMemoryData() {
        System.err.println("Memory data:");
        System.err.println();

        Set<HeapValue> heapValues = connection.getTotalHeapUsage();
        for (HeapValue heapValue : heapValues) {
            String className = heapValue.getDescriptor(connection).getDescription();
            // print instance counts for two selected classes that are created  by the test program
            if (className.equals("java.util.Random") || className.endsWith("$TestObject")) {
                System.err.println(className + ": " + heapValue.getInstanceCount() + " instances");
            }
        }
        System.err.println();
    }

    private static void dumpHotSpots(ThreadInfo mainThread) {

        System.err.println("Hot spots:");
        System.err.println();

        // Calculate method hot spots for the "Runnable" thread status for the main thread
        CpuHotspots cpuHotspots = connection.getCpuHotspots(Collections.singleton(mainThread), Aggregation.METHOD,
                ThreadStatus.RUNNING, true);

        List<ProfilingValue> hotspots = cpuHotspots.getHotspots();

        // Hot spots are unsorted by default
        Collections.sort(hotspots);

        // The total time, needed for percentage calculations
        long totalTime = cpuHotspots.getTotalTime();

        // print the first 5 hot spots with backtraces
        System.err.println("Top 5 hot spots:");
        for (int i = 0; i < Math.min(5, hotspots.size()); i++) {

            ProfilingValue profilingValue = hotspots.get(i);

            Descriptor descriptor = profilingValue.getDescriptor(connection);

            System.err.print("Hot spot " + (i + 1) + ": ");
            System.err.print(descriptor.getDescription());
            System.err.print(": ");
            // time measurements are in microseconds
            System.err.print(profilingValue.getValue() / 1000);
            System.err.print(" ms (");
            System.err.print(Math.round(1000f * profilingValue.getValue() / totalTime) / 10);
            System.err.print(" %)");
            System.err.println();
            System.err.println("Backtraces: ");

            // You can print call trees with a helper class contained in the JProfiler platform
            System.err.println(TreePrinter.getIndentedTree(connection, connection.getCpuHotspotBacktrace(profilingValue)));
        }
    }

    private static void dumpCallTree() {
        System.err.println("Cpu tree:");
        System.err.println();

        // Calculate call tree for thread states cumulated for all threads aggregated on the method level
        Tree tree = connection.getCpuTree(null, Aggregation.METHOD, ThreadStatus.ALL);

        // You can print call trees with a helper class contained in the JProfiler platform
        System.err.println(TreePrinter.getIndentedTree(connection, tree));
    }

    private static void dumpPayloadHotSpots() {

        System.err.println("Custom payload hot spots:");
        System.err.println();

        // Calculate payload hot spots for all thread states for the main thread, back traces are aggregated on the class level
        Collection<? extends ProfilingValue> hotspots = connection.getPayloadHotspots(LoadFactorProbe.class.getName(), null, Aggregation.CLASS, ThreadStatus.ALL);

        // Hot spots are unsorted by default
        List<? extends ProfilingValue> sortedHotspots = new ArrayList<>(hotspots);
        Collections.sort(sortedHotspots);

        // print all hot spots with back traces
        System.err.println("Tested load factors:");
        for (int i = 0; i < sortedHotspots.size(); i++) {

            ProfilingValue profilingValue = sortedHotspots.get(i);

            Descriptor descriptor = profilingValue.getDescriptor(connection);

            System.err.print("#" + (i + 1) + ": ");
            System.err.print(descriptor.getDescription());
            System.err.print(": ");
            // time measurements are in microseconds
            System.err.print(profilingValue.getValue() / 1000);
            System.err.print(" ms, count: " + profilingValue.getCount());

            System.err.println();

        }
        System.err.println();
    }
}
