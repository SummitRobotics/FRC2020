package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.logging.Logger;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Constants.LoggerRelations;


public class OI implements Logger{
    XboxController controller = new XboxController(Constants.XBOX_PORT);

    //array of all controler inputs
    double[] valuesToLog = new double[18];

    //wpi compatable butttons
    public Button buttonA = new Button(){
        @Override
        public boolean get(){
        return LogPassBool(LoggerRelations.BTN_A, controller.getAButton());  
        }                                                                                    
    };

    public Button buttonB = new Button(){
        @Override
        public boolean get(){
        return LogPassBool(LoggerRelations.BTN_B, controller.getBButton());  
        }                                                                                             
    };

    public Button buttonX = new Button(){
        @Override
        public boolean get() {
        return  LogPassBool(LoggerRelations.BTN_X, controller.getXButton());
        }
    };

    public Button buttonY = new Button(){
        @Override
        public boolean get() {
        return LogPassBool(LoggerRelations.BTN_Y, controller.getYButton());    
        }                                                                             
    };

    public Button buttonStart = new Button(){
        @Override
        public boolean get() {
        return LogPassBool(LoggerRelations.BTN_START, controller.getStartButton());
        }
    };

    public Button buttonBack = new Button(){
        @Override
        public boolean get() {
        return LogPassBool(LoggerRelations.BTN_BACK, controller.getBackButton());
        }
    };

    //dpad - angle gets what buttons are pressed in a stupid way so that is un-done
    public Button dpadUp = new Button(){
        @Override
        public boolean get() {
        int angle = controller.getPOV();// gets dpad "angle"
        boolean out = false;
        if(angle == 0 || angle == 45 || angle == 315){out = true;}// if up or up and right/left is pressed make out true
        return LogPassBool(LoggerRelations.DPAD_UP, out);           
        }                                                                              
    };

    public Button dpadDown = new Button(){
        @Override
        public boolean get() {
        int angle = controller.getPOV();// gets dpad "angle"
        boolean out = false;
        if(angle == 180 || angle == 135 || angle == 225){out = true;}// if down or down and right/left is pressed make out true
        return LogPassBool(LoggerRelations.DPAD_DOWN, out); 
        }                                                                                        
    };

    public Button dpadLeft = new Button(){
        @Override
        public boolean get() {
        int angle = controller.getPOV();// gets dpad "angle"
        boolean out = false;
        if(angle == 270 || angle == 225 || angle == 315){out = true;}// if left or left and down/up is pressed make out true
        return LogPassBool(LoggerRelations.DPAD_LEFT, out);     
        }                                                                                    
    };

    public Button dpadRight = new Button(){
        @Override
        public boolean get() {
        int angle = controller.getPOV();// gets dpad "angle"
        boolean out = false;
        if(angle == 90 || angle == 45 || angle == 135){out = true;}// if right or right and down/up is pressed make out true
        return LogPassBool(LoggerRelations.DPAD_RIGHT, out); 
        }                                                                                       
    };

    //bumpers
    public Button rightBumper = new Button(){
        @Override
        public boolean get() {
        return LogPassBool(LoggerRelations.RIGHT_BUMPER, controller.getBumper(Hand.kRight));
        }
    };

    public Button leftBumper = new Button(){
        @Override
        public boolean get() {
        return LogPassBool(LoggerRelations.LEFT_BUMPER, controller.getBumper(Hand.kLeft));
        }
    };

    public Button leftBumperInverted = new Button(){
        @Override
        public boolean get() {
        return !LogPassBool(LoggerRelations.LEFT_BUMPER, controller.getBumper(Hand.kLeft));
        }
    };

    //trigers
    public double rightTriger(){
        return LogPassDouble(LoggerRelations.RIGHT_TRIGGER, controller.getTriggerAxis(Hand.kRight));
    }

    public double leftTriger(){
        return LogPassDouble(LoggerRelations.LEFT_TRIGGER, controller.getTriggerAxis(Hand.kLeft));
    }

    //sticks - x is left and right and y is up and down
    public double left_Stick_X(){
        return LogPassDouble(LoggerRelations.LEFT_STICK_X, controller.getX(Hand.kLeft));
    }

    public double left_Stick_Y(){
        return LogPassDouble(LoggerRelations.LEFT_STICK_Y, controller.getY(Hand.kLeft));
    }

    public double right_Stick_X(){
        return LogPassDouble(LoggerRelations.RIGHT_STICK_X, controller.getX(Hand.kRight));
    }

    public double right_Stick_Y(){
        return LogPassDouble(LoggerRelations.RIGHT_STICK_Y, controller.getY(Hand.kRight));
    }

    //functions to make logging simple

    private boolean LogPassBool(LoggerRelations index, boolean in){
        //converts in to double and puts it into the array
        valuesToLog[index.value] = (in? 1.0 : 0.0);
        return in;
    }

    private double LogPassDouble(LoggerRelations index, double in){
        //puts double into the array
        valuesToLog[index.value] = in;
        return in;
    }

    //logging function
    @Override
    public double[] getValues(double[] values) {
        //runs through the values to log array and sets values eaqual to it
        for(int i = 0; i<valuesToLog.length; i++){
            values[i] = valuesToLog[i];
        }
        return values;
    }




}