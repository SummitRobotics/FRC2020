import com.jprofiler.api.probe.embedded.PayloadProbe;
import com.jprofiler.api.probe.embedded.telemetry.Telemetry;

// This class defines the probe that analyzes the cache. It is a payload probe and annotates
// arbitrary strings into the call tree. The JEE & probes section will get a separate probe view
// named "Factorial cache" whose call tree and hot hot spots views show the recorded payloads.
public class FactorialCacheProbe extends PayloadProbe {

    @Override
    public String getName() {
        return "Factorial cache";
    }

    @Override
    public String getDescription() {
        return "Records request to the factorial cache";
    }

    // A method annotated with @Telemetry is polled once per second for a numeric value and is added to the
    // "Telemetry" view of the probe.
    // The other telemetries in that view are created automatically based on the payload recording.
    @Telemetry("Cache put operations")
    public static int getPutCount() {
        return FactorialCache.getPutCount();
    }
}
