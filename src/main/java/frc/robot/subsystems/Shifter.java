package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.Ports;

public class Shifter implements Subsystem, Logger{

    private DoubleSolenoid shift;
    private boolean oldShift;

    public Shifter() {
        shift = new DoubleSolenoid(Ports.PCM_1, Ports.SHIFT_SOLENOID_UP, Ports.SHIFT_SOLENOID_DOWN);
    }

    public void highGear() {
        oldShift = true;
        shift.set(Value.kForward);
    }

    public void lowGear() {
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