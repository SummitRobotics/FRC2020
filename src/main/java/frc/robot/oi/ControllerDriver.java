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

	public LoggerButton
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

    public LoggerAxis
    leftX,
    leftY,
    leftTrigger,
    rightX,
    rightY,
    rightTrigger;

	public ControllerDriver(int port) {
		super(port);

        buttonA = generateLoggerButton(Button.kA.value);

		buttonA = generateLoggerButton(Button.kA.value);
		buttonB = generateLoggerButton(Button.kB.value);
		buttonX = generateLoggerButton(Button.kX.value);
		buttonY = generateLoggerButton(Button.kY.value);
		buttonStart = generateLoggerButton(Button.kStart.value);
		buttonBack = generateLoggerButton(Button.kBack.value);
		rightBumper = generateLoggerButton(Button.kBumperRight.value);
		leftBumper = generateLoggerButton(Button.kBumperLeft.value);

		dPadUp = generateLoggerButton(getDPadValue(DPadValues.UP));
		dPadDown = generateLoggerButton(getDPadValue(DPadValues.DOWN));
		dPadLeft = generateLoggerButton(getDPadValue(DPadValues.LEFT));
        dPadRight = generateLoggerButton(getDPadValue(DPadValues.RIGHT));
        
        leftX = generateLoggerAxis(0);
        leftY = generateLoggerAxis(1);
        leftTrigger = generateLoggerAxis(2);
        rightTrigger = generateLoggerAxis(3);
        rightX = generateLoggerAxis(4);
        rightY = generateLoggerAxis(5);
	}

	private ButtonGetter getDPadValue(DPadValues value) {
		return () -> value.isEqual(getPOV());
	}
}