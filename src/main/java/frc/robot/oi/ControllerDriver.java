package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.Ports;
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

	public ControllerDriver(Ports port, SyncLogger logger) {
        this.port = port.port;
        this.logger = logger;

        buttonA = generateLoggerButton(Button.kA.value, LoggerRelations.BTN_A);

		buttonA = generateLoggerButton(Button.kA.value, LoggerRelations.BTN_A);
		buttonB = generateLoggerButton(Button.kB.value, LoggerRelations.BTN_B);
		buttonX = generateLoggerButton(Button.kX.value, LoggerRelations.BTN_X);
		buttonY = generateLoggerButton(Button.kY.value, LoggerRelations.BTN_Y);
		buttonStart = generateLoggerButton(Button.kStart.value, LoggerRelations.BTN_START);
		buttonBack = generateLoggerButton(Button.kBack.value, LoggerRelations.BTN_BACK);
		rightBumper = generateLoggerButton(Button.kBumperRight.value, LoggerRelations.RIGHT_BUMPER);
		leftBumper = generateLoggerButton(Button.kBumperLeft.value, LoggerRelations.LEFT_BUMPER);

		dPadUp = generateLoggerButton(getDPadValue(DPadValues.UP), LoggerRelations.DPAD_UP);
		dPadDown = generateLoggerButton(getDPadValue(DPadValues.DOWN), LoggerRelations.DPAD_DOWN);
		dPadLeft = generateLoggerButton(getDPadValue(DPadValues.LEFT), LoggerRelations.DPAD_LEFT);
        dPadRight = generateLoggerButton(getDPadValue(DPadValues.RIGHT), LoggerRelations.DPAD_RIGHT);
        
        leftX = generateLoggerAxis(0, LoggerRelations.LEFT_STICK_X);
        leftY = generateLoggerAxis(1, LoggerRelations.LEFT_STICK_Y);
        leftTrigger = generateLoggerAxis(2, LoggerRelations.LEFT_TRIGGER);
        rightX = generateLoggerAxis(3, LoggerRelations.RIGHT_STICK_X);
        rightY = generateLoggerAxis(4, LoggerRelations.RIGHT_STICK_Y);
        rightTrigger = generateLoggerAxis(5, LoggerRelations.RIGHT_TRIGGER);
	}

	private ButtonGetter getDPadValue(DPadValues value) {
		return () -> value.isEqual(getPOV());
	}
}