import com.jprofiler.api.probe.embedded.PayloadProbe;
import com.jprofiler.api.probe.embedded.telemetry.Telemetry;
import com.jprofiler.api.probe.embedded.telemetry.TelemetryFormat;
import com.jprofiler.api.probe.embedded.telemetry.TelemetryUnit;

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

    // Control objects are only supported if you override this method to return true.
    @Override
    public boolean isControlObjects() {
        return true;
    }

    // Custom types can group payload events into different types and color the control object
    // timeline accordingly. The custom type for a payload is specified when calling
    // Payload.enter().
    @Override
    public Class<? extends Enum> getCustomTypes() {
        return CacheOperationType.class;
    }

    // If control objects have not been opened explicitly via Payload.openControlObject(), this
    // method is queried to get a name for the control object.
    @Override
    public String getControlObjectName(Object controlObject) {
        if (controlObject instanceof FactorialCacheConnection) {
            return "Factorial cache connection";
        } else {
            return null;
        }
    }

    // A method annotated with @Telemetry is polled once per second for a numeric value and is added to the
    // "Telemetry" view of the probe.
    //
    // Several telemetry lines can be combined into a single telemetry by giving them the same name, but
    // different "line" parameter values. If the values are additive, like in this case, they can also be stacked.
    // The other telemetries in that view are created automatically based on the payload recording.
    @Telemetry(value = "Cache operations", line = "Get", format = @TelemetryFormat(value = TelemetryUnit.PER_SECOND, stacked = true))
    public static int getGetCount() {
        // When this method is called, a new interval is started, so the counter is returned and reset at the same time.
        return FactorialCache.getGetCount();
    }

    @Telemetry(value = "Cache operations", line = "Check", format = @TelemetryFormat(value = TelemetryUnit.PER_SECOND, stacked = true))
    public static int getCheckCount() {
        return FactorialCache.getCheckCount();
    }

    @Telemetry(value = "Cache operations", line = "Put", format = @TelemetryFormat(value = TelemetryUnit.PER_SECOND, stacked = true))
    public static int getPutCount() {
        return FactorialCache.getPutCount();
    }
}
