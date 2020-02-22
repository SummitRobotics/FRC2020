package frc.robot.oi;

import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * Wrappper class for the TI Launchpad in mode 1
 */
public class LaunchpadDriver extends GenericDriver {

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

    missileA,
    missileB;

    public LoggerAxis
    axisA,
    axisB,
    axisC,
    axisD,
    axisE,
    axisF,
    axisG,
    axisH;


    public LaunchpadDriver(int port, SyncLogger logger) {
		super(port, logger);

        //TODO - create actual logger values
        buttonA = generateLEDButton(1, LoggerRelations.PLACEHOLDER);
        buttonB = generateLEDButton(2, LoggerRelations.PLACEHOLDER);
        buttonC = generateLEDButton(3, LoggerRelations.PLACEHOLDER);
        buttonD = generateLEDButton(4, LoggerRelations.PLACEHOLDER);
        buttonE = generateLEDButton(5, LoggerRelations.PLACEHOLDER);
        buttonF = generateLEDButton(6, LoggerRelations.PLACEHOLDER);
        buttonG = generateLEDButton(7, LoggerRelations.PLACEHOLDER);
        buttonH = generateLEDButton(8, LoggerRelations.PLACEHOLDER);
        buttonI = generateLEDButton(9, LoggerRelations.PLACEHOLDER);

        missileA = generateLoggerButton(10, LoggerRelations.PLACEHOLDER);
        missileB = generateLoggerButton(11, LoggerRelations.PLACEHOLDER);

        axisA = generateLoggerAxis(1, LoggerRelations.PLACEHOLDER);
        axisB = generateLoggerAxis(2, LoggerRelations.PLACEHOLDER);
        axisC = generateLoggerAxis(3, LoggerRelations.PLACEHOLDER);
        axisD = generateLoggerAxis(4, LoggerRelations.PLACEHOLDER);
        axisE = generateLoggerAxis(5, LoggerRelations.PLACEHOLDER);
        axisF = generateLoggerAxis(6, LoggerRelations.PLACEHOLDER);
        axisG = generateLoggerAxis(7, LoggerRelations.PLACEHOLDER);
        axisH = generateLoggerAxis(8, LoggerRelations.PLACEHOLDER);
    }

    protected LEDButton generateLEDButton(int output, LoggerRelations logReference) {
		if (logger != null) {
			return new LEDButton(
                getButtonGetter(output), 
                logReference, 
                logger, 
                (boolean state) -> setOutput(output, state)
            );
		}

		return new LEDButton(
            getButtonGetter(output), 
            logReference, 
            (boolean state) -> setOutput(output, state)
        );
	}

    /**
     * Black box to set outputs
     * 
     * @param outputNumber the output number
     * @param value the state of the output
     */
    public void setOutput(int outputNumber, boolean value) {
        outputs = (outputs & ~(1 << (outputNumber - 1))) | ((value ? 1 : 0) << (outputNumber - 1));
        HAL.setJoystickOutputs((byte) port, outputs, (short)0, (short)0);
    }
}