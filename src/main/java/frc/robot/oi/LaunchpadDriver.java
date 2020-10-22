package frc.robot.oi;

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

    public OIButton
    missileA,
    missileB,

    funLeft,
    funMiddle,
    funRight;

    public OIAxis
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

    public LaunchpadDriver(int port) {
		super(port);

        buttonA = generateLEDButton(1);
        buttonB = generateLEDButton(2);
        buttonC = generateLEDButton(3);
        buttonD = generateLEDButton(4);
        buttonE = generateLEDButton(5);
        buttonF = generateLEDButton(6);
        buttonG = generateLEDButton(7);
        buttonH = generateLEDButton(8);
        buttonI = generateLEDButton(9);

        missileA = generateOIButton(10);
        missileB = generateOIButton(11);

        bigLEDGreen = getLEDLambda(10);
        bigLEDRed = getLEDLambda(11);

        axisA = generateOIAxis(0);
        axisB = generateOIAxis(1);

        axisA.setDeadzone(0);
        axisB.setDeadzone(0);

        reee = getAxisGetter(2);

        funLeft = generateATDButton(2, -1, -1/3);
        funMiddle = generateATDButton(2, -1/3, 1/3);
        funRight = generateATDButton(2, 1/3, 1);

        axisC = generateOIAxis(3);
        axisD = generateOIAxis(4);
        axisE = generateOIAxis(5);
        axisF = generateOIAxis(6);
        axisG = generateOIAxis(7);
        axisH = generateOIAxis(8);
    }

    protected LEDButton generateLEDButton(int output) {
		return new LEDButton(
            getButtonGetter(output), 
            (boolean state) -> setOutput(output, state)
        );
    }
    
    private LED getLEDLambda(int output) {
        return (boolean state) -> setOutput(output, state);
    }

    private OIButton generateATDButton(int output, int min, int max) {
        AxisGetter axis = getAxisGetter(output);
        return new OIButton(() -> {
            double value = axis.get();
            return value >= min && max >= value;
        });
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