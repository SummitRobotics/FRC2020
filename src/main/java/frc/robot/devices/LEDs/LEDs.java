package frc.robot.devices.LEDs;

import java.util.HashMap;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Ports;

public class LEDs extends SubsystemBase {

    private AddressableLED ledStrip;
    private AddressableLEDBuffer buffer;

    private HashMap<String, LEDCall> calls;
    private boolean callsOutOfDate;

    private int loop;

    public LEDs() {
        ledStrip = new AddressableLED(Ports.LED_PORT);
        buffer = new AddressableLEDBuffer(Ports.LED_LENGTH);

        ledStrip.setLength(Ports.LED_LENGTH);
        ledStrip.start();

        calls = new HashMap<>();
        callsOutOfDate = true;

        loop = 0;
    }

    private void applyChanges() {
        ledStrip.setData(buffer);
    }

    public void addCall(String name, LEDCall call) {
        callsOutOfDate = true;
        calls.putIfAbsent(name, call);
    }

    public void removeCall(String name) {
        callsOutOfDate = true;
        calls.remove(name);
    }

    private void reassignCalls() {
        for (LEDRange.Atomic atom : LEDRange.Atomic.values()) {
            atom.refreshCalls();
        }

        for (LEDCall call : calls.values()) {
            for (LEDRange.Atomic atom : call.getRange().getAtoms()) {
                atom.updateCall(call);
            }
        }
    }

    @Override
    public void periodic() {
        loop++;

        if (callsOutOfDate) {
            reassignCalls();
            callsOutOfDate = false;
        }

        for (LEDRange.Atomic atom : LEDRange.Atomic.values()) {
            atom.updateLEDs(buffer, loop);
        }

        applyChanges();
    }
}