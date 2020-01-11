package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.logging.Logger;
import frc.robot.utilities.Constants;

public class Shifter implements Subsystem, Logger{

    private Compressor compressor = new Compressor(Constants.PCM_1);
    private Solenoid shiftHigh = new Solenoid(Constants.PCM_1, Constants.DRIVE_SOLENOID_OPEN);
    private Solenoid shiftLow = new Solenoid(Constants.PCM_1, Constants.DRIVE_SOLENOID_CLOSE);
    private boolean oldShift;

    public Shifter(){
        compressor.setClosedLoopControl(true);
        oldShift = shiftHigh.get();
    }

    /**
     * controls the shifting of the drivetrain on the robot
     * @param state true is low gear, flase is high grear
     */
    public void Shift(boolean state){
        if(state != oldShift){
            if(state){
                shiftHigh.set(true);
                shiftLow.set(false);
            }
            else{
                shiftHigh.set(false);
                shiftLow.set(true);
            }
            oldShift = state;
        }
    }

    public boolean getShiftState(){
        return oldShift;
    }

    public double[] getValues(double[] values){
        values[Constants.LoggerRelations.SHIFT_STATE.value] = oldShift? 1.0 : 0.0;

        return values;
    }

}