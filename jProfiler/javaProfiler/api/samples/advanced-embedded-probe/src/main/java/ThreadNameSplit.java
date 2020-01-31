import com.jprofiler.api.probe.embedded.SplitProbe;

// This is a split probe that does not create payloads. It only splits the call tree tree for the current thread name.
// The probe class does not contain any logic, it is only used as a parameter for Split.enter().
public class ThreadNameSplit extends SplitProbe {
}
