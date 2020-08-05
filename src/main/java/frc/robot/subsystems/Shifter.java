package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.Colors;
import frc.robot.utilities.Ports;

public class Shifter implements Subsystem, Logger{

    private DoubleSolenoid shift;
    private boolean oldShift;
    private LEDRange ShiftLeds;
    private int LEDpriority = 5;

    public Shifter(LEDRange leds) {
        ShiftLeds = leds;
        shift = new DoubleSolenoid(Ports.PCM_1, Ports.SHIFT_SOLENOID_UP, Ports.SHIFT_SOLENOID_DOWN);
    }

    public void highGear() {
        ShiftLeds.removeLEDCall("lowShift");
        oldShift = true;
        shift.set(Value.kForward);
    }

    public void lowGear() {
        ShiftLeds.addLEDCall("lowShift", new LEDCall(LEDpriority, LEDCall.solid(Colors.Red)));
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

    public double[] getValues(double[] values){
        values[LoggerRelations.SHIFT_STATE.value] = oldShift? 1.0 : 0.0;

        return values;
    }

}