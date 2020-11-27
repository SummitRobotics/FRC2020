package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.utilities.Colors;
import frc.robot.utilities.Ports;

public class Shifter implements Subsystem {

    private DoubleSolenoid shift;
    private boolean oldShift;
    private LEDs Leds;
    private int LEDpriority = 5;

    public Shifter(LEDs leds) {
        this.Leds = leds;
        shift = new DoubleSolenoid(Ports.PCM_1, Ports.SHIFT_SOLENOID_UP, Ports.SHIFT_SOLENOID_DOWN);
    }

    public void highGear() {
        Leds.removeCall("lowShift");
        oldShift = true;
        shift.set(Value.kForward);
    }

    public void lowGear() {
        Leds.addCall("lowShift", new LEDCall(LEDpriority, LEDRange.All).sine(Colors.Red));
        oldShift = false;
        shift.set(Value.kReverse);
    }

    /**
     * Getes the shift state
     * 
     * @return the shift state
     */
    public boolean getShiftState(){
        return oldShift;
    }
}