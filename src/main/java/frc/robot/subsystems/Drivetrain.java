package frc.robot.subsystems;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.devices.PigeonGyro;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Subsystem to control the drivetrain of the robot
 */
public class Drivetrain implements Subsystem {

    public final static DifferentialDriveKinematics DRIVE_KINEMATICS = new DifferentialDriveKinematics(0); //TODO - get correct track width

    // left motors
    private final CANSparkMax left = new CANSparkMax(Ports.LEFT_DRIVE_MAIN, MotorType.kBrushless);
    private final CANSparkMax leftMiddle = new CANSparkMax(Ports.LEFT_DRIVE_0, MotorType.kBrushless);
    private final CANSparkMax leftBack = new CANSparkMax(Ports.LEFT_DRIVE_1, MotorType.kBrushless);

    // right motors
    private final CANSparkMax right = new CANSparkMax(Ports.RIGHT_DRIVE_MAIN, MotorType.kBrushless);
    private final CANSparkMax rightMiddle = new CANSparkMax(Ports.RIGHT_DRIVE_0, MotorType.kBrushless);
    private final CANSparkMax rightBack = new CANSparkMax(Ports.RIGHT_DRIVE_1, MotorType.kBrushless);

    // pid controllers
    private final CANPIDController leftPID = left.getPIDController();
    private final CANPIDController rightPID = right.getPIDController();

    // encoders
    private final CANEncoder leftEncoder = left.getEncoder();
    private final CANEncoder rightEncoder = right.getEncoder();

    private final PigeonGyro gyro;
    private final DifferentialDriveOdometry odometry;

    public Drivetrain(PigeonGyro gyro) {
        // tells other two motors to follow the first
        leftMiddle.follow(left);
        leftBack.follow(left);

        rightMiddle.follow(right);
        rightBack.follow(right);

        // inverts right side
        left.setInverted(true);
        right.setInverted(false);

        // sets pid values
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
        
        left.disableVoltageCompensation();
        right.disableVoltageCompensation();
        
        leftMiddle.disableVoltageCompensation();
        rightMiddle.disableVoltageCompensation();

        leftBack.disableVoltageCompensation();
        rightBack.disableVoltageCompensation();

        this.gyro = gyro;
        odometry = new DifferentialDriveOdometry(new Rotation2d(0));
    }

    /**
     * Sets the power of the left side of the drivetrain
     * 
     * @param power -1 - 1
     */
    public void setLeftMotorPower(double power) {
        power = Functions.clampDouble(power, 1.0, -1.0);
        left.set(power);
    }

    /**
     * Sets the power of the right side of the drivetrain
     * 
     * @param power -1 - 1
     */
    public void setRightMotorPower(double power) {
        power = Functions.clampDouble(power, 1.0, -1.0);
        right.set(power);
    }

    /**
     * Sets the target position of the left side of the drivetrain
     * 
     * @param position the target position in terms of motor rotations
     */
    public void setLeftMotorPositionTarget(double position) {
        leftPID.setReference(position, ControlType.kPosition);
    }

    /**
     * Sets the target position of the right side of the drivetrain
     * 
     * @param position the target position in terms of motor rotations
     */
    public void setRightMotorPositionTarget(double position) {
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
    public void setRightEncoder(double position) {
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
        left.setOpenLoopRampRate(rate);
        right.setOpenLoopRampRate(rate);
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

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public void setMotorVelocityTargets(double left, double right) {
        leftPID.setReference(left, ControlType.kVelocity);
        rightPID.setReference(right, ControlType.kVelocity);
    }

    @Override
    public void periodic() {
        odometry.update(new Rotation2d(gyro.getHeading()), leftEncoder.getPosition(), rightEncoder.getPosition());
    }
}