import com.jprofiler.api.probe.embedded.TypeCustomizer;

// Enum for custom probe types that is passed to Payload.enter().
// For each type, the control object time line is colored in a different way.
// The event type is shown in the events view where you can also filter by event types.
// By implementing TypeCustomizer, you can specify the colors explicitly, otherwise the colors
// would be assigned automatically.
public enum CacheOperationType implements TypeCustomizer {
    READ("Read", 0x00CC00),
    WRITE("Write", 0xCC0000);

    private String name;
    private int color;

    CacheOperationType(String name, int color) {
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
