package frc.robot.oi;

import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.Ports;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Wrappper class for the TI Launchpad in mode 1
 */
public class LaunchpadDriver extends GenericDriver implements Subsystem {

    private int port;
    private int outputs;

    public LoggerButton
    buttonA,
    buttonB,
    buttonC,
    buttonD,
    buttonE,
    buttonF,
    buttonG,
    buttonH,
    buttonI,
    buttonJ,
    buttonK;

    public LoggerAxis
    axisA,
    axisB,
    axisC,
    axisD,
    axisE,
    axisF,
    axisG,
    axisH;


    public LaunchpadDriver(Ports port, SyncLogger logger) {
        this.port = port.port;
        this.logger = logger;

        //TODO - create actual logger values
        buttonA = generateLoggerButton(1, LoggerRelations.PLACEHOLDER);
        buttonB = generateLoggerButton(2, LoggerRelations.PLACEHOLDER);
        buttonC = generateLoggerButton(3, LoggerRelations.PLACEHOLDER);
        buttonD = generateLoggerButton(4, LoggerRelations.PLACEHOLDER);
        buttonE = generateLoggerButton(5, LoggerRelations.PLACEHOLDER);
        buttonF = generateLoggerButton(6, LoggerRelations.PLACEHOLDER);
        buttonG = generateLoggerButton(7, LoggerRelations.PLACEHOLDER);
        buttonH = generateLoggerButton(8, LoggerRelations.PLACEHOLDER);
        buttonI = generateLoggerButton(9, LoggerRelations.PLACEHOLDER);
        buttonJ = generateLoggerButton(10, LoggerRelations.PLACEHOLDER);
        buttonK = generateLoggerButton(11, LoggerRelations.PLACEHOLDER);

        axisA = generateLoggerAxis(1, LoggerRelations.PLACEHOLDER);
        axisB = generateLoggerAxis(2, LoggerRelations.PLACEHOLDER);
        axisC = generateLoggerAxis(3, LoggerRelations.PLACEHOLDER);
        axisD = generateLoggerAxis(4, LoggerRelations.PLACEHOLDER);
        axisE = generateLoggerAxis(5, LoggerRelations.PLACEHOLDER);
        axisF = generateLoggerAxis(6, LoggerRelations.PLACEHOLDER);
        axisG = generateLoggerAxis(7, LoggerRelations.PLACEHOLDER);
        axisH = generateLoggerAxis(8, LoggerRelations.PLACEHOLDER);
    }

    //outputs
    public void setOutput1(boolean state){
        setOutput(1, state);
    }

    public void setOutput2(boolean state){
        setOutput(2, state);
    }

    public void setOutput3(boolean state){
        setOutput(3, state);
    }

    public void setOutput4(boolean state){
        setOutput(4, state);
    }

    public void setOutput5(boolean state){
        setOutput(5, state);
    }

    public void setOutput6(boolean state){
        setOutput(6, state);
    }

    public void setOutput7(boolean state){
        setOutput(7, state);
    }

    public void setOutput8(boolean state){
        setOutput(8, state);
    }

    public void setOutput9(boolean state){
        setOutput(9, state);
    }

    public void setOutput10(boolean state){
        setOutput(10, state);
    }

    public void setOutput11(boolean state){
        setOutput(11, state);
    }

    /**
     * Black box to set outputs
     * @param outputNumber the output number
     * @param value the state of the output
     */
    public void setOutput(int outputNumber, boolean value) {
        outputs = (outputs & ~(1 << (outputNumber - 1))) | ((value ? 1 : 0) << (outputNumber - 1));
        HAL.setJoystickOutputs((byte) port, outputs, (short)0, (short)0);
    }
}