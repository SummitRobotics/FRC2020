import com.jprofiler.api.probe.injected.TypeCustomizer;

// Enum for custom probe types that is passed to probeContext.createPayload().
// For each type, the control object time line is colored in a different way.
// The event type is shown in the events view where you can also filter by event types.
// By implementing TypeCustomizer, you can specify the colors explicitly, otherwise the colors
// would be assigned automatically.
public enum AwtEventType implements TypeCustomizer {
    REPAINT("Repaint", 0x00CC00),
    INVOCATION("Invocation event", 0xCC00CC),
    COMPONENT("Component event", 0xCC0000),
    OTHER("Other event", 0x0000CC);

    private String name;
    private int color;

    AwtEventType(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }
}
