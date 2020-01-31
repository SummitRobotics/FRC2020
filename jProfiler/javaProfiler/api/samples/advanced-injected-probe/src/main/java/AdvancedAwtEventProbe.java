import com.jprofiler.api.probe.injected.ControlObjectName;
import com.jprofiler.api.probe.injected.Payload;
import com.jprofiler.api.probe.injected.Probe;
import com.jprofiler.api.probe.injected.ProbeContext;
import com.jprofiler.api.probe.injected.interception.*;
import com.jprofiler.api.probe.injected.parameter.Parameter;
import com.jprofiler.api.probe.injected.parameter.ThisValue;
import com.jprofiler.api.probe.injected.telemetry.Telemetry;
import com.jprofiler.api.probe.injected.telemetry.TelemetryFormat;
import com.jprofiler.api.probe.injected.telemetry.TelemetryUnit;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.InvocationEvent;
import java.util.concurrent.atomic.AtomicInteger;

// A simpler version of the same kind of probe is available in the "simple-injected-probe" sample. It may
// be a good idea to try to understand the simple version first before looking at this more advanced version.
//
// This is a sample probe that inserts itself in into all calls to java.awt.EventQueue.dispatchEvent(java.awt.AWTEvent event)
// and monitors AWT events based on their source objects with a special focus on repaints.
// Note the "controlObjects" and "customTypes" flags in the probe annotation that are required to switch on support
// for these features.
//
// The call tree and hot spots views of this probe do not show any interesting call stacks because the AWT event
// handling begins at the top of the call stack. This is a probe that measures "incoming work load" while
// interesting call stacks would usually be observed in a probe that measures "outgoing work load", such as a
// database driver probe.
//
@Probe(name = "AWT event types", description = "AWT events split for each source component", controlObjects = true, customTypes = AwtEventType.class)
public class AdvancedAwtEventProbe {

    // Instead of returning a string like for a simple payload probe, this probe returns a
    // Payload object that also specifies the associated control object and a special event type.
    // Control objects are long-lived objects that create logical groups for events. In this case,
    // the event source is taken as such a control object. The most typical case for control objects
    // is a database connection.
    @PayloadInterception(invokeOn = InvocationType.ENTER,
            method = @MethodSpec(className = "java.awt.EventQueue", methodName = "dispatchEvent",
                    parameterTypes = {"java.awt.AWTEvent"}, returnType = "void"))
    public static Payload dispatchEvent(ProbeContext probeContext, @Parameter(0) AWTEvent event) {
        return probeContext.createPayload(event.getClass().getName(), event.getSource(), getEventType(event));
    }

    // Thread local state variable that keeps track of whether the "repaint" method is currently executing.
    private static final ThreadLocal<Boolean> inRepaint = ThreadLocal.withInitial(() -> Boolean.FALSE);

    // This is a nested interception that occurs deeper in the call stack than "dispatchEvent". The returned
    // Payload object replaces the payload of the outer instrumentation but keeps its timing. The ProbeContext
    // object gives you access to the outer payload and offers different strategies for timing.
    @PayloadInterception(invokeOn = InvocationType.ENTER,
            method = @MethodSpec(className = "javax.swing.JComponent", methodName = "paintImmediately",
                    parameterTypes = {"int", "int", "int", "int"}, returnType = "void"))
    public static Payload repaint(ProbeContext probeContext, @ThisValue JComponent component) {
        inRepaint.set(Boolean.TRUE);
        // If the "paint" is nested in a "repaint", we don't count it as a child paint.
        repaintCount.incrementAndGet();
        return probeContext.createPayload("Repaint " + getSourceName(component), component, AwtEventType.REPAINT);
    }

    // We want to keep track of when "repaint" is being executed, but as requested by the "invokeOn" annotation
    // parameter, the code in "repaint" is inserted when the target method is entered. To reset the "inRepaint" variable,
    // we have to execute some code when the same method is exited. That's what the  @AdditionalInterception annotation
    // is for. It instruments the same method as the previous handler with a different InvocationType.
    @AdditionalInterception(InvocationType.EXIT)
    public static void repaintExit() {
        inRepaint.set(Boolean.FALSE);
    }

    // The @Interception annotation is intended to execute code for bookkeeping purposes or to gather information
    // for other handlers. In this case, we want to increment a counter unless the instrumented method is invoked
    // during a repaint. Note the "subtypes" parameter on the annotation that attaches the interceptor to
    // all overridden methods as well. @Interception handlers are reentrant by default, unlike
    // @PayloadInterception or @SplitInterception handlers.
    @Interception(invokeOn = InvocationType.ENTER,
            method = @MethodSpec(className = "javax.swing.JComponent", methodName = "paint",
                    parameterTypes = {"java.awt.Graphics"}, returnType = "void", subtypes = true))
    public static void paint() {
        // If the "paint" is nested in a "repaint", we don't count it as a child paint.
        if (!inRepaint.get()) {
            childPaintCount.incrementAndGet();
        }
    }

    // Usually, control objects should be opened with probeContext.openControlObject(). If new control objects
    // are added dynamically as parameters to probeContext.createPayload(), a method annotated with
    // @ControlObjectName and this signature returns the desired names for the control objects.
    @ControlObjectName
    public static String getSourceName(Object source) {
        StringBuilder builder = new StringBuilder();
        builder.append(source.getClass().getName());
        String extraName = getExtraName(source);
        if (extraName != null) {
            builder.append(" [\"").append(extraName).append("\"]");
        }
        return builder.toString();
    }

    private static AwtEventType getEventType(AWTEvent event) {
        if (event instanceof InvocationEvent) {
            return AwtEventType.INVOCATION;
        } else if (event instanceof ComponentEvent) {
            return AwtEventType.COMPONENT;
        } else {
            return AwtEventType.OTHER;
        }
    }

    private static String getExtraName(Object source) {
        if (source instanceof JTextComponent) {
            return ((JTextComponent)source).getText();
        } else if (source instanceof AbstractButton) {
            return ((AbstractButton)source).getText();
        } else if (source instanceof Component) {
            return ((Component)source).getName();
        } else {
            return null;
        }
    }

    // A method that is annotated with @SplitInterception splits the call tree for each distinct return value,
    // so you can analyze the call tree separately for each value. This is similar to the URL call tree splitting
    // in JProfiler that is done by the servlet probe.
    // By default, a split interception produces payloads from the splits, in this case this is not desirable,
    // because the same payloads are created by dispatchEvent. With the "payloads" annotation parameter we can switch
    // off payload creation.
    @SplitInterception(method = @MethodSpec(className = "java.awt.EventQueue", methodName = "dispatchEvent",
            parameterTypes = {"java.awt.AWTEvent"}, returnType = "void"), payloads = false)
    public static String splitDispatchEvent(@Parameter(0) AWTEvent event) {
        return event.getClass().getName();
    }

    // Call tree splittings can be nested, like in this case, where the repaint split is nested within the event
    // class split. Note the "reentrant" parameter on the annotation that allows recursive invocations to produce
    // further call tree splits.
    @SplitInterception(method = @MethodSpec(className = "javax.swing.JComponent", methodName = "paintImmediately",
            parameterTypes = {"int", "int", "int", "int"}, returnType = "void"),
            reentrant = true)
    public static String splitDispatchEvent(@ThisValue JComponent component) {
        return "Repaint " + getSourceName(component);
    }

    // A method annotated with @Telemetry is polled once per second for a numeric value and is added to the
    // "Telemetry" view of the probe.
    //
    // Several telemetry lines can be combined into a single telemetry by giving them the same name, but
    // different "line" parameter values. If the values are additive, like in this case, they can also be stacked.
    // The other telemetries in that view are created automatically based on the payload recording.
    @Telemetry(value = "Paint operations", line = "Repaints", format = @TelemetryFormat(value = TelemetryUnit.PER_SECOND, stacked = true))
    public static int getRepaintCount() {
        // When this method is called, a new interval is started, so the counter is returned and reset at the same time.
        return repaintCount.getAndSet(0);
    }

    // Resize the window of the animated Bezier curve sample to see child component paints in the stacked telemetry.
    @Telemetry(value = "Paint operations", line = "Child component paints", format = @TelemetryFormat(value = TelemetryUnit.PER_SECOND, stacked = true))
    public static int getChildPaintCount() {
        return childPaintCount.getAndSet(0);
    }

    // Counters for the telemetry. The number classes from the java.util.concurrent.atomic package
    // are a thread-safe way to maintain such counters
    private static final AtomicInteger childPaintCount = new AtomicInteger();
    private static final AtomicInteger repaintCount = new AtomicInteger();
}
