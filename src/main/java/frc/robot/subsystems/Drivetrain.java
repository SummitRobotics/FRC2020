package frc.robot.subsystems;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.lists.Ports;
import java.util.function.BooleanSupplier;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Subsystem to control the drivetrain of the robot
 */
public class Drivetrain extends SubsystemBase {

    private static final double
    LowP = 2.29,
    LowI = 0.0,
    LowD = 0.0,
    LowKs = 0.177,
    LowKv = 1.55,
    LowKa = 0.133,
    HighP = 2.85,
    HighI = 0.0,
    HighD = 0.0,
    HighKs = 0.211,
    HighKv = 0.734,
    HighKa = 0.141,
    HighGearRatio = 9.1,
    LowGEarRatio = 19.65,
    WheleRadiusInMeters = 0.0762,
    wheleCirconfranceInMeters = (2*WheleRadiusInMeters)*Math.PI,
    MaxOutputVoltage = 11,
    //TODO check if right (is in meters)
    DriveWidth = 1.5;

    // left motors
    private CANSparkMax left = new CANSparkMax(Ports.LEFT_DRIVE_3, MotorType.kBrushless);
    private CANSparkMax leftMiddle = new CANSparkMax(Ports.LEFT_DRIVE_2, MotorType.kBrushless);
    private CANSparkMax leftBack = new CANSparkMax(Ports.LEFT_DRIVE_1, MotorType.kBrushless);

    // right motors
    private CANSparkMax right = new CANSparkMax(Ports.RIGHT_DRIVE_3, MotorType.kBrushless);
    private CANSparkMax rightMiddle = new CANSparkMax(Ports.RIGHT_DRIVE_2, MotorType.kBrushless);
    private CANSparkMax rightBack = new CANSparkMax(Ports.RIGHT_DRIVE_1, MotorType.kBrushless);

    // pid controllers
    private CANPIDController leftPID = left.getPIDController();
    private CANPIDController rightPID = right.getPIDController();

    // encoders
    private CANEncoder leftEncoder = left.getEncoder();
    private CANEncoder rightEncoder = right.getEncoder();

    private DifferentialDriveOdometry odometry;

    private BooleanSupplier shift;
    private AHRS gyro;

    public static DifferentialDriveKinematics DriveKinimatics = new DifferentialDriveKinematics(DriveWidth);

    public static SimpleMotorFeedforward HighFeedFoward = new SimpleMotorFeedforward(HighKs, HighKv, HighKa);

    public static SimpleMotorFeedforward LowFeedFoward = new SimpleMotorFeedforward(LowKs, LowKv, LowKa);

    public static DifferentialDriveVoltageConstraint HighVoltageConstraint = new DifferentialDriveVoltageConstraint(HighFeedFoward, DriveKinimatics, MaxOutputVoltage);

    public static DifferentialDriveVoltageConstraint LowVoltageConstraint = new DifferentialDriveVoltageConstraint(LowFeedFoward, DriveKinimatics, MaxOutputVoltage);

    /**
     * i am in PAIN wow this is BAD
     * @param gyro odimetry is bad
     * @param shiftState shifting was a mistake
     * just be glad i am trying to limit my sins and am not passing in the whole shift object in and using fake callbacks form hell
     */
    public Drivetrain(AHRS gyro, BooleanSupplier shiftState) {

        this.gyro = gyro;
        this.shift = shiftState;

        odometry = new DifferentialDriveOdometry(gyro.getRotation2d());

        // tells other two motors to follow the first
        leftMiddle.follow(left);
        leftBack.follow(left);

        rightMiddle.follow(right);
        rightBack.follow(right);

        // inverts right side
        left.setInverted(true);
        right.setInverted(false);

        // sets pid values
        zeroEncoders();

        leftPID.setP(0.05);
        leftPID.setI(0);
        leftPID.setD(0);
        leftPID.setOutputRange(-.25, .25);
        
        rightPID.setP(0.05);
        rightPID.setI(0);
        rightPID.setD(0);
        rightPID.setOutputRange(-.25, .25);

        left.disableVoltageCompensation();
        right.disableVoltageCompensation();
        
        leftMiddle.disableVoltageCompensation();
        rightMiddle.disableVoltageCompensation();

        leftBack.disableVoltageCompensation();
        rightBack.disableVoltageCompensation();

        setClosedRampRate(0);
        setOpenRampRate(0);

        left.setSmartCurrentLimit(40);
        leftMiddle.setSmartCurrentLimit(40);
        leftBack.setSmartCurrentLimit(40);

        right.setSmartCurrentLimit(40);
        rightMiddle.setSmartCurrentLimit(40);
        rightBack.setSmartCurrentLimit(40);

        left.setIdleMode(IdleMode.kBrake);
        leftMiddle.setIdleMode(IdleMode.kBrake);
        leftBack.setIdleMode(IdleMode.kBrake);

        right.setIdleMode(IdleMode.kBrake);
        rightMiddle.setIdleMode(IdleMode.kBrake);
        rightBack.setIdleMode(IdleMode.kBrake);
    
    }

    /**
     * Sets the power of the left side of the drivetrain
     * 
     * @param power -1 - 1
     */
    public synchronized void setLeftMotorPower(double power) {
        power = Functions.clampDouble(power, 1.0, -1.0);
        left.set(power);
    }

    /**
     * Sets the power of the right side of the drivetrain
     * 
     * @param power -1 - 1
     */
    public synchronized void setRightMotorPower(double power) {
        power = Functions.clampDouble(power, 1.0, -1.0);
        right.set(power);
    }

    public synchronized void setLeftMotorVolts(double volts){
        left.setVoltage(volts);
    }

    public synchronized void setRightMotorVolts(double volts){
        right.setVoltage(volts);
    }

    public void setMotorVolts(double left, double right){
        System.out.println(String.format("left is: %f, right is %f", left, right));
        setRightMotorVolts(right);
        setLeftMotorVolts(left);
    }

    /**
     * Sets the target position of the left side of the drivetrain
     * 
     * @param position the target position in terms of motor rotations
     */
    public synchronized void setLeftMotorTarget(double position) {
        leftPID.setReference(position, ControlType.kPosition);
    }

    /**
     * Sets the target position of the right side of the drivetrain
     * 
     * @param position the target position in terms of motor rotations
     */
    public synchronized void setRightMotorTarget(double position) {
        rightPID.setReference(position, ControlType.kPosition);
    }

    /**
     * The position you want the left side to register when it is in the position it
     * is currently in
     * 
     * @param position the position for the encoder to register in rotations
     */
    public synchronized void setLeftEncoder(double position) {
        leftEncoder.setPosition(position);
    }

    /**
     * The position you want the right side to register when it is in the position
     * it is currently in
     * 
     * @param position the position for the encoder to register in rotations
     */
    public synchronized void setRightEncoder(double position) {
        rightEncoder.setPosition(position);
    }

    public synchronized void zeroEncoders(){
        setRightEncoder(0);
        setLeftEncoder(0);
    }


    /**
     * Returns the current position of right side of the drivetrain
     * 
     * @return position of motor in rotations
     */
    public synchronized double getRightEncoderPosition() {
        return rightEncoder.getPosition();
    }

    /**
     * Returns the current position of right side of the drivetrain
     * 
     * @return position of motor in rotations
     */
    public synchronized double getLeftEncoderPosition() {
        return leftEncoder.getPosition();
    }

    public synchronized double getLeftRPM(){
        return leftEncoder.getVelocity();
    }

    public synchronized double getRightRPM(){
        return rightEncoder.getVelocity();
    }

    /**
     * @return the total distance in meters the side as travled sense the last reset
     */
    public synchronized double getLeftDistance(){
        if(shift.getAsBoolean()){
            return (getLeftEncoderPosition()/HighGearRatio)*wheleCirconfranceInMeters;
        }
        else{
            return (getLeftEncoderPosition()/LowGEarRatio)*wheleCirconfranceInMeters;
        }
    }

     /**
     * @return the total distance in meters the side as travled sense the last reset
     */
    public synchronized double getRightDistance(){
        if(shift.getAsBoolean()){
            return (getRightEncoderPosition()/HighGearRatio)*wheleCirconfranceInMeters;
        }
        else{
            return (getRightEncoderPosition()/LowGEarRatio)*wheleCirconfranceInMeters;
        }
    }

    /**
     * @return the linear speed of the side in meters per second
     */
    public synchronized double getLeftSpeed(){
        if(shift.getAsBoolean()){
            return covertRpmToMetersPerSencond((getLeftRPM()/HighGearRatio));
        }
        else{
            return covertRpmToMetersPerSencond((getLeftRPM()/LowGEarRatio));
        }
    }
    /**
     * @return the linear speed of the side in meters per second
     */
    public synchronized double getRightSpeed(){
        if(shift.getAsBoolean()){
            return covertRpmToMetersPerSencond((getRightRPM()/HighGearRatio));
        }
        else{
            return covertRpmToMetersPerSencond((getRightRPM()/LowGEarRatio));
        }
    }

    //could things be good for once please
    private synchronized double covertRpmToMetersPerSencond(double RPM){
        return ((RPM/60)*(2*Math.PI))*WheleRadiusInMeters;
    }

    /**
     * Sets the rate at witch the motors ramp up and down in open loop control mode
     * 
     * @param rate time in seconds to go from 0 to full power
     */
    public synchronized void setOpenRampRate(double rate) {
        left.setOpenLoopRampRate(rate);
        right.setOpenLoopRampRate(rate);
    }

    /**
     * Sets the rate at which the motors ramp up and down in closed loop control
     * mode
     * 
     * @param rate time in seconds to go from 0 to full power
     */
    public synchronized void setClosedRampRate(double rate) {
        left.setClosedLoopRampRate(rate);
        right.setClosedLoopRampRate(rate);
    }

    /**
     * Stops the motors
     */
    public synchronized void stop() {
        left.stopMotor();
        right.stopMotor();
    }

    public synchronized DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(getLeftSpeed(), getRightSpeed());
    }

    /**
     * sets the curent pos and RESETS ENCODERS TO 0
     * @param pose the new pose
     */
    public synchronized void setPose(Pose2d pose) {
        zeroEncoders();
        odometry.resetPosition(pose, gyro.getRotation2d());
      }

    public synchronized Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    /**
     * gets the right pid values for the curent shift state
     * @return double array of p,i,d
     */
    public synchronized double[] getPid(){
        if(shift.getAsBoolean()){
            double[] out = {HighP, HighI, HighD};
            return out;
        }
        else{
            double[] out = {LowP, LowI, LowD};
            return out;
        }
    }

    public synchronized boolean getShift(){
        return shift.getAsBoolean();
    }

    public synchronized SimpleMotorFeedforward getFeedFoward(){
        if(shift.getAsBoolean()){
            return HighFeedFoward;
        }
        else{
            return LowFeedFoward;
        }
    }

    public synchronized DifferentialDriveVoltageConstraint getVoltageConstraint(){
        if(shift.getAsBoolean()){
            return HighVoltageConstraint;
        }
        else{
            return LowVoltageConstraint;
        }
    }


    public synchronized void periodic() {
        // Update the odometry in the periodic block
        odometry.update(gyro.getRotation2d(), getLeftDistance(), getRightDistance());
    }
}