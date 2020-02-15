package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.Ports;

public class Shifter implements Subsystem, Logger{

    //TODO - make double solenoid

    private Compressor compressor = new Compressor(Ports.PCM_1.port);
    private Solenoid shiftHigh = new Solenoid(Ports.PCM_1.port, Ports.DRIVE_SOLENOID_OPEN.port);
    private Solenoid shiftLow = new Solenoid(Ports.PCM_1.port, Ports.DRIVE_SOLENOID_CLOSE.port);
    private boolean oldShift;

    public Shifter(){
        compressor.setClosedLoopControl(true);
        oldShift = shiftHigh.get();
    }

    /**
     * controls the shifting of the drivetrain on the robot
     * 
     * @param state true is low gear, false is high grear
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