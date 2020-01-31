import com.jprofiler.api.probe.embedded.PayloadProbe;

public class LoadFactorProbe extends PayloadProbe {
    @Override
    public String getName() {
        return "Hash map load factors";
    }
}
