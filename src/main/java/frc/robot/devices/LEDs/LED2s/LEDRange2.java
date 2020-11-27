package frc.robot.devices.LEDs.LED2s;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public enum LEDRange2 {
    LeftClimb(Atomic.LeftClimb),
    RightClimb(Atomic.RightClimb),
    Middle(Atomic.Middle),
    LeftIntake(Atomic.LeftIntake),
    RightIntake(Atomic.RightIntake),
    BothClimb(Atomic.LeftClimb, Atomic.RightClimb),
    BothIntake(Atomic.LeftIntake, Atomic.RightIntake),
    All(
        Atomic.LeftClimb, 
        Atomic.RightClimb, 
        Atomic.Middle, 
        Atomic.LeftIntake, 
        Atomic.RightIntake
    );

    private Atomic[] ranges;

    LEDRange2(Atomic... ranges) {
        this.ranges = ranges;
    }

    public Atomic[] getAtoms() {
        return ranges;
    }

    
    protected enum Atomic {
        LeftClimb(34, 57),
        RightClimb(0, 23),
        Middle(24, 33),
        LeftIntake(58, 76),
        RightIntake(77, 96);

        private int start;
        private int end;

        private final Color8Bit defaultColor;
        private LEDCall2 call;

        Atomic(int start, int end) {
            this.start = start;
            this.end = end;

            defaultColor = new Color8Bit(Color.kBlack);
            call = null;
        }

        public void refreshCalls() {
            call = null;
        }

        public void updateCall(LEDCall2 newCall) {
            if (call == null) {
                call = newCall;
            } else {
                if (call.getPriority() < newCall.getPriority()) {
                    call = newCall;
                }
            }
        }

        public void updateLEDs(AddressableLEDBuffer buffer, int loop) {
            if (call == null) {
                for (int i = start; i <= end; i++) {
                    buffer.setLED(i, defaultColor);
                }
            } else {
                for (int i = start; i <= end; i++) {
                    buffer.setLED(i, call.getColor(loop, i));
                }
            }
        }
    }
}