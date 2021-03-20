package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.lists.LEDPriorities;
import frc.robot.utilities.lists.Ports;

public class Shifter extends SubsystemBase {

    private DoubleSolenoid shift;
    private boolean;

    private LEDCall lowShift = new LEDCall(LEDPriorities.lowGear, LEDRange.All).sine(Colors.Red);

    public Shifter() {
        shift = new DoubleSolenoid(Ports.PCM_1, Ports.SHIFT_SOLENOID_UP, Ports.SHIFT_SOLENOID_DOWN);
    }

    public void highGear() {
        lowShift.cancel();
        oldShift = true;
        shift.set(Value.kForward);
    }

    public void lowGear() {
        lowShift.activate();
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