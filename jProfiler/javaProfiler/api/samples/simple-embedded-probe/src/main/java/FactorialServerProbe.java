import com.jprofiler.api.probe.embedded.SplitProbe;

// This class defines the probe that analyzes the requests to the server. It is a split probe and
// shows different types of requests separately in the CPU call tree.
// The probe class does not contain any logic, it is only used as a parameter for Split.enter().
public class FactorialServerProbe extends SplitProbe {

    @Override
    public String getName() {
        return "Factorial server";
    }

    @Override
    public String getDescription() {
        return "Splits the call tree for unique factorial calculations";
    }

    // By default a split probe does not create its own probe view and only splits all call trees in the existing views.
    // However, you can ask a split probe to create payloads from the split strings. Because we do so here, the
    // JEE & probes section will get a separate probe view named "Factorial server" whose call tree and hot spots
    // views show the recorded payloads.
    @Override
    public boolean isPayloads() {
        return true;
    }
}
