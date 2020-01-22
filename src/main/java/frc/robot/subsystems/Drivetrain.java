package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.logging.Logger;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Functions;
import frc.robot.utilities.powerlimiting.LimitedSubsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Subsystem to control the drivetrain of the robot
 */
public class Drivetrain implements Subsystem, Logger, LimitedSubsystem {

    //data for logger
    private double rightMotorPower = 0, leftMotorPower = 0, rightMotorTarget = 0, leftMotorTarget = 0;

    // left motors
    private CANSparkMax left = new CANSparkMax(Constants.LEFT_DRIVE_MAIN, MotorType.kBrushless);
    private CANSparkMax leftMiddle = new CANSparkMax(Constants.LEFT_DRIVE_0, MotorType.kBrushless);
    private CANSparkMax leftBack = new CANSparkMax(Constants.LEFT_DRIVE_1, MotorType.kBrushless);

    // right motors
    private CANSparkMax right = new CANSparkMax(Constants.RIGHT_DRIVE_MAIN, MotorType.kBrushless);
    private CANSparkMax rightMiddle = new CANSparkMax(Constants.RIGHT_DRIVE_0, MotorType.kBrushless);
    private CANSparkMax rightBack = new CANSparkMax(Constants.RIGHT_DRIVE_1, MotorType.kBrushless);

    // pid controllers
    private CANPIDController leftPID = left.getPIDController();
    private CANPIDController rightPID = right.getPIDController();

    // encoders
    private CANEncoder leftEncoder = left.getEncoder();
    private CANEncoder rightEncoder = right.getEncoder();

    private double oldOpenRampRate; // the previous ramp rate sent to the motors
    private double oldClosedRampRate; // the previous ramp rate sent to the motors

    // pid config
    private double 
    FEED_FWD = 0, 
    I_ZONE = 0,
    // change later, just so a problem doesn't break my walls
    OUTPUT_MIN = -.25, 
    OUTPUT_MAX = .25;

    
    @Override
    public double[] getValues(double[] values) {
        values[Constants.LoggerRelations.LEFT_MOTOR_POWER.value] = leftMotorPower;
        values[Constants.LoggerRelations.RIGHT_MOTOR_POWER.value] = rightMotorPower;
        values[Constants.LoggerRelations.LEFT_MOTOR_TARGET.value] = leftMotorTarget;
        values[Constants.LoggerRelations.RIGHT_MOTOR_TARGET.value] = rightMotorTarget;
        values[Constants.LoggerRelations.LEFT_MOTOR_POSITION.value] = getLeftEncoderPosition();
        values[Constants.LoggerRelations.RIGHT_MOTOR_POSITION.value] = getRightEncoderPosition();
        
        return values;
    }

    
    public double getPriority(){
        return 1;
    }
    public void limitPower(double amount){
        int cl = (int)(500*amount);
        System.out.println(cl);
        left.setSecondaryCurrentLimit(cl);
        leftBack.setSecondaryCurrentLimit(cl);
        leftMiddle.setSecondaryCurrentLimit(cl);

        right.setSecondaryCurrentLimit(cl);
        rightBack.setSecondaryCurrentLimit(cl);
        rightMiddle.setSecondaryCurrentLimit(cl);
    }

    public Drivetrain() {
        // tells other two motors to follow the first
        leftMiddle.follow(left);
        leftBack.follow(left);

        rightMiddle.follow(right);
        rightBack.follow(right);

        // inverts right side
        left.setInverted(true);

        // sets pid values
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
        leftPID.setP(Constants.DRIVETRAIN_P);
        leftPID.setI(Constants.DRIVETRAIN_I);
        leftPID.setD(Constants.DRIVETRAIN_D);
        leftPID.setFF(FEED_FWD);
        leftPID.setIZone(I_ZONE);
        leftPID.setOutputRange(OUTPUT_MIN, OUTPUT_MAX);
        rightPID.setP(Constants.DRIVETRAIN_P);
        rightPID.setI(Constants.DRIVETRAIN_I);
        rightPID.setD(Constants.DRIVETRAIN_D);
        rightPID.setFF(FEED_FWD);
        rightPID.setIZone(I_ZONE);
        rightPID.setOutputRange(OUTPUT_MIN, OUTPUT_MAX);
    }

    /**
     * Sets the power of the left side of the drivetrain
     * 
     * @param power -1 - 1
     */
    public void setLeftMotorPower(double power) {
        power = Functions.clampDouble(power, 1.0, -1.0);
        leftMotorPower = power;
        left.set(power);
    }

    /**
     * Sets the power of the right side of the drivetrain
     * 
     * @param power -1 - 1
     */
    public void setRightMotorPower(double power) {
        power = Functions.clampDouble(power, 1.0, -1.0);
        rightMotorPower = power;
        right.set(power);
    }

    /**
     * Sets the target position of the left side of the drivetrain
     * 
     * @param position the target position in terms of motor rotations
     */
    public void setLeftMotorPosition(double position) {
        leftMotorPower = 2;
        leftMotorTarget = position;
        leftPID.setReference(position, ControlType.kPosition);
    }

    /**
     * Sets the target position of the right side of the drivetrain
     * 
     * @param position the target position in terms of motor rotations
     */
    public void setRightMotorPosition(double position) {
        rightMotorPower = 2;
        rightMotorTarget = position;
        rightPID.setReference(position, ControlType.kPosition);
    }

    /**
     * The position you want the left side to register when it is in the position it
     * is currently in
     * 
     * @param position the position for the encoder to register in rotations
     */
    public void setLeftEncoder(int position) {
        leftEncoder.setPosition(position);
    }

    /**
     * The position you want the right side to register when it is in the position
     * it is currently in
     * 
     * @param position the position for the encoder to register in rotations
     */
    public void setRightEncoder(int position) {
        rightEncoder.setPosition(position);
    }

    /**
     * Returns the current position of right side of the drivetrain
     * 
     * @return position of motor in rotations
     */
    public double getRightEncoderPosition() {
        return rightEncoder.getPosition();
    }

    /**
     * Returns the current position of right side of the drivetrain
     * 
     * @return position of motor in rotations
     */
    public double getLeftEncoderPosition() {
        return leftEncoder.getPosition();
    }

    /**
     * Sets the rate at witch the motors ramp up and down in open loop control mode
     * 
     * @param rate time in seconds to go from 0 to full power
     */
    public void setOpenRampRate(double rate) {
        // checks against old ramp rate to prevent unnecessary ramp-rate sets at they take
        // lots of cpu time
        if (rate != oldOpenRampRate) {
            left.setOpenLoopRampRate(rate);
            right.setOpenLoopRampRate(rate);
            oldOpenRampRate = rate;
        }
    }

    /**
     * Sets the rate at which the motors ramp up and down in closed loop control
     * mode
     * 
     * @param rate time in seconds to go from 0 to full power
     */
    public void setClosedRampRate(double rate) {
        // checks against old ramp rate to prevent unnecessary ramp-rate sets at they take
        // lots of cpu time
        if (rate != oldClosedRampRate) {
            left.setClosedLoopRampRate(rate);
            right.setClosedLoopRampRate(rate);
            oldClosedRampRate = rate;
        }
    }

    /**
     * Stops the motors
     */
    public void stop() {
        left.stopMotor();
        right.stopMotor();
    }

    /**
     * Makes the robot turn by a power
     * 
     * @param power power of the motors
     */
    public void turn(double power) {
        setLeftMotorPower(-power);
        setRightMotorPower(power);
    }
}