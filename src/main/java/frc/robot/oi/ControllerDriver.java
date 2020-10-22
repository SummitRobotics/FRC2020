package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.utilities.functionalinterfaces.ButtonGetter;

/**
 * Wrapper class for XBox controllers
 */
public class ControllerDriver extends GenericDriver {

	public enum DPadValues {
        UP(0, 45, 315),
        DOWN(180, 135, 225),
        LEFT(90, 45, 135),
        RIGHT(270, 225, 315);

        public int[] values;
        private DPadValues(int... values) {
            this.values = values;
        }

        public boolean isEqual(int value) {
            for (int element : values) {
                if (value == element) {
                    return true;
                }
            }
            return false;
        }
    }

	public OIButton
    buttonA,
    buttonB,
    buttonX,
    buttonY,
    buttonStart,
    buttonBack,
    rightBumper,
    leftBumper,

    dPadUp,
    dPadDown,
    dPadLeft,
    dPadRight;

    public OIAxis
    leftX,
    leftY,
    leftTrigger,
    rightX,
    rightY,
    rightTrigger;

	public ControllerDriver(int port) {
		super(port);

        buttonA = generateOIButton(Button.kA.value);

		buttonA = generateOIButton(Button.kA.value);
		buttonB = generateOIButton(Button.kB.value);
		buttonX = generateOIButton(Button.kX.value);
		buttonY = generateOIButton(Button.kY.value);
		buttonStart = generateOIButton(Button.kStart.value);
		buttonBack = generateOIButton(Button.kBack.value);
		rightBumper = generateOIButton(Button.kBumperRight.value);
		leftBumper = generateOIButton(Button.kBumperLeft.value);

		dPadUp = new OIButton(getDPadValue(DPadValues.UP));
		dPadDown = new OIButton(getDPadValue(DPadValues.DOWN));
		dPadLeft = new OIButton(getDPadValue(DPadValues.LEFT));
        dPadRight = new OIButton(getDPadValue(DPadValues.RIGHT));
        
        leftX = generateOIAxis(0);
        leftY = generateOIAxis(1);
        leftTrigger = generateOIAxis(2);
        rightTrigger = generateOIAxis(3);
        rightX = generateOIAxis(4);
        rightY = generateOIAxis(5);
	}

	private ButtonGetter getDPadValue(DPadValues value) {
		return () -> value.isEqual(getPOV());
	}
}