import com.jprofiler.api.probe.injected.Probe;
import com.jprofiler.api.probe.injected.interception.InvocationType;
import com.jprofiler.api.probe.injected.interception.MethodSpec;
import com.jprofiler.api.probe.injected.interception.PayloadInterception;
import com.jprofiler.api.probe.injected.interception.SplitInterception;
import com.jprofiler.api.probe.injected.parameter.Parameter;
import com.jprofiler.api.probe.injected.telemetry.Telemetry;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.util.concurrent.atomic.AtomicInteger;

// A sample probe that inserts itself in into all calls to java.awt.EventQueue.dispatchEvent(java.awt.AWTEvent event)
// records the class name of the event parameter and attaches this data to the call tree.
//
// A more elaborate example of the same kind of probe can be found in the "advanced-injected-probe" sample.
@Probe(name = "AWT event types", description = "AWT event classes")
public class SimpleAwtEventProbe {

    // A method that is annotated with @PayloadInterception can return a string with the payload data
    // that should be annotated in the call tree. In the probe view, the call tree and hot spot views
    // will show this data.
    @PayloadInterception(invokeOn = InvocationType.ENTER,
            method = @MethodSpec(className = "java.awt.EventQueue", methodName = "dispatchEvent",
                    parameterTypes = {"java.awt.AWTEvent"}, returnType = "void"))
    public static String eventClassNamePayload(@Parameter(0) AWTEvent event) {
        // In the signature of the method you can request different kind of data, in this case only the
        // first parameter of the method, annotated with the @Parameter(0).
        // The package com.jprofiler.api.probe.injected.parameters offers other annotations of this kind.
        if (event instanceof ComponentEvent) {
            // Count component events for the telemetry
            componentEventCount.incrementAndGet();
        }
        return event.getClass().getName();
    }

    // A method that is annotated with @SplitInterception splits the call tree for each distinct return value,
    // so you can analyze the call tree separately for each value. This is similar to the URL call tree splitting
    // in JProfiler that is done by the servlet probe.
    @SplitInterception(method = @MethodSpec(className = "java.awt.EventQueue", methodName = "dispatchEvent",
            parameterTypes = {"java.awt.AWTEvent"}, returnType = "void"))
    public static String eventClassNamePayloadSplit(@Parameter(0) AWTEvent event) {
        return event.getClass().getName();
    }

    // A method annotated with @Telemetry is polled once per second for a numeric value and is added to the
    // "Telemetry" view of the probe.
    // The other telemetries in that view are created automatically based on the payload recording.
    @Telemetry("Component events")
    public static int getComponentEvents() {
        // When this method is called, a new interval is started, so the counter is returned and reset at the same time.
        return componentEventCount.getAndSet(0);
    }

    // Counter for the telemetry. The number classes from the java.util.concurrent.atomic package
    // are a thread-safe way to maintain such counters
    private static final AtomicInteger componentEventCount = new AtomicInteger();

}
