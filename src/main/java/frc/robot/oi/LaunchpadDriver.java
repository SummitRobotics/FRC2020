package frc.robot.oi;

import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.oi.LEDButton.LED;
import frc.robot.utilities.functionalinterfaces.AxisGetter;
import edu.wpi.first.hal.HAL;

/**
 * Wrappper class for the TI Launchpad in mode 1
 */
public class LaunchpadDriver extends GenericDriver {

    private int outputs;

    public LEDButton
    buttonA,
    buttonB,
    buttonC,
    buttonD,
    buttonE,
    buttonF,
    buttonG,
    buttonH,
    buttonI;

    public LoggerButton
    missileA,
    missileB,

    funLeft,
    funMiddle,
    funRight;

    public LoggerAxis
    axisA,
    axisB,
    axisC,
    axisD,
    axisE,
    axisF,
    axisG,
    axisH;

    public AxisGetter reee;

    public LED
    bigLEDGreen,
    bigLEDRed;

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

        bigLEDGreen = getLEDLambda(10);
        bigLEDRed = getLEDLambda(11);

        axisA = generateLoggerAxis(0, LoggerRelations.PLACEHOLDER);
        axisB = generateLoggerAxis(1, LoggerRelations.PLACEHOLDER);

        axisA.setDeadzone(0);
        axisB.setDeadzone(0);

        reee = getAxisGetter(2);

        funLeft = generateATDButton(2, -1, -1/3, LoggerRelations.PLACEHOLDER);
        funMiddle = generateATDButton(2, -1/3, 1/3, LoggerRelations.PLACEHOLDER);
        funRight = generateATDButton(2, 1/3, 1, LoggerRelations.PLACEHOLDER);

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
    
    private LED getLEDLambda(int output) {
        return (boolean state) -> setOutput(output, state);
    }

    private LoggerButton generateATDButton(int output, int min, int max, LoggerRelations logReference) {
        AxisGetter axis = getAxisGetter(output);
        return generateLoggerButton(() -> {
            double value = axis.get();
            return value >= min && max >= value;
        }, logReference);
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