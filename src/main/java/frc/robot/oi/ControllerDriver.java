package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.Ports;


public class ControllerDriver implements Logger {

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

    XboxController controller;

    public LoggerButton
    buttonA,
    buttonB,
    buttonX,
    buttonY,
    buttonStart,
    buttonBack,

    dPadUp,
    dPadDown,
    dPadLeft,
    dPadRight,

    rightBumper,
    leftBumper;

    public ControllerDriver(SyncLogger logger) {

        controller = new XboxController(Ports.XBOX_PORT.port);

        buttonA = new LoggerButton(controller::getAButton, LoggerRelations.BTN_A);
        buttonB = new LoggerButton(controller::getBButton, LoggerRelations.BTN_B);
        buttonX = new LoggerButton(controller::getXButton, LoggerRelations.BTN_X);
        buttonY = new LoggerButton(controller::getYButton, LoggerRelations.BTN_Y);

        buttonStart = new LoggerButton(controller::getStartButton, LoggerRelations.BTN_START);
        buttonBack = new LoggerButton(controller::getBackButton, LoggerRelations.BTN_BACK);

        dPadUp = new LoggerButton(() -> DPadValues.UP.isEqual(controller.getPOV()), LoggerRelations.DPAD_UP);
        dPadDown = new LoggerButton(() -> DPadValues.DOWN.isEqual(controller.getPOV()), LoggerRelations.DPAD_DOWN);
        dPadLeft = new LoggerButton(() -> DPadValues.LEFT.isEqual(controller.getPOV()), LoggerRelations.DPAD_LEFT);
        dPadRight = new LoggerButton(() -> DPadValues.RIGHT.isEqual(controller.getPOV()), LoggerRelations.DPAD_RIGHT);

        rightBumper = new LoggerButton(() -> controller.getBumper(Hand.kRight), LoggerRelations.RIGHT_BUMPER);
        leftBumper = new LoggerButton(() -> controller.getBumper(Hand.kLeft), LoggerRelations.LEFT_BUMPER);

        logger.addElements(
            this,
            buttonA,
            buttonB,
            buttonX,
            buttonY,
            buttonStart,
            buttonBack,
            dPadUp,
            dPadDown,
            dPadLeft,
            dPadRight,
            rightBumper,
            leftBumper
        );
    }

    //trigers
    public double rightTrigger(){
        return controller.getTriggerAxis(Hand.kRight);
    }

    public double leftTrigger(){
        return controller.getTriggerAxis(Hand.kLeft);
    }

    //sticks - x is left and right and y is up and down
    public double leftStickX(){
        return controller.getX(Hand.kLeft);
    }

    public double leftStickY(){
        return controller.getY(Hand.kLeft);
    }

    public double rightStickX(){
        return controller.getX(Hand.kRight);
    }

    public double rightStickY(){
        return controller.getY(Hand.kRight);
    }

    @Override
    public double[] getValues(double[] values) {
        values[LoggerRelations.RIGHT_TRIGGER.value] = rightTrigger();
        values[LoggerRelations.LEFT_TRIGGER.value] = leftTrigger();
        values[LoggerRelations.LEFT_STICK_X.value] = leftStickX();
        values[LoggerRelations.LEFT_STICK_Y.value] = leftStickY();
        values[LoggerRelations.RIGHT_STICK_X.value] = rightStickX();
        values[LoggerRelations.RIGHT_STICK_Y.value] = rightStickY();

        return values;
    }
}