package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.logging.Logger;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.Constants.DPadValues;
import frc.robot.utilities.Constants.LoggerRelations;


public class ControllerDriver implements Logger{

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

    public ControllerDriver(SyncLogger logger, int controllerPort) {

        controller = new XboxController(controllerPort);

        //wpi compatable butttons
        buttonA = new LoggerButton(LoggerRelations.BTN_A, logger) {
            @Override
            public boolean get(){
                return controller.getAButton();
            }                                                                                    
        };

        //wpi compatable butttons
        buttonB = new LoggerButton(LoggerRelations.BTN_B, logger) {
            @Override
            public boolean get(){
                return controller.getBButton();
            }                                                                                    
        };

        //wpi compatable butttons
        buttonX = new LoggerButton(LoggerRelations.BTN_X, logger) {
            @Override
            public boolean get(){
                return controller.getXButton();
            }                                                                                    
        };

        //wpi compatable butttons
        buttonY = new LoggerButton(LoggerRelations.BTN_Y, logger) {
            @Override
            public boolean get(){
                return controller.getYButton();
            }                                                                                    
        };

        //wpi compatable butttons
        buttonStart = new LoggerButton(LoggerRelations.BTN_START, logger) {
            @Override
            public boolean get(){
                return controller.getStartButton();
            }                                                                                    
        };

        //wpi compatable butttons
        buttonBack = new LoggerButton(LoggerRelations.BTN_BACK, logger) {
            @Override
            public boolean get(){
                return controller.getBackButton();
            }                                                                                    
        };

        dPadUp = new LoggerButton(LoggerRelations.DPAD_UP, logger) {
            @Override
            public boolean get() {
                return DPadValues.UP.isEqual(controller.getPOV());
            }
        };

        dPadDown = new LoggerButton(LoggerRelations.DPAD_DOWN, logger) {
            @Override
            public boolean get() {
                return DPadValues.DOWN.isEqual(controller.getPOV());
            }
        };

        dPadLeft = new LoggerButton(LoggerRelations.DPAD_LEFT, logger) {
            @Override
            public boolean get() {
                return DPadValues.LEFT.isEqual(controller.getPOV());
            }
        };

        dPadRight = new LoggerButton(LoggerRelations.DPAD_RIGHT, logger) {
            @Override
            public boolean get() {
                return DPadValues.RIGHT.isEqual(controller.getPOV());
            }
        };

        rightBumper = new LoggerButton(LoggerRelations.RIGHT_BUMPER, logger) {
            @Override
            public boolean get() {
                return controller.getBumper(Hand.kRight);
            }
        };

        leftBumper = new LoggerButton(LoggerRelations.LEFT_BUMPER, logger) {
            @Override
            public boolean get() {
                return controller.getBumper(Hand.kLeft);
            }
        };

        logger.addElements(this);
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

    public class LoggerButton extends Button implements Logger {
        private LoggerRelations logReference;

        public LoggerButton(LoggerRelations logReference, SyncLogger logger) {
            super();
            this.logReference = logReference;
            
            logger.addElements(this);
        }
    
        @Override
        public double[] getValues(double[] values) {
            values[logReference.value] = super.get() ? 1 : 0;
            return values;
        }    
    }
}