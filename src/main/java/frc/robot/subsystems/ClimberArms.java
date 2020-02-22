package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.Ports;

/**
 * Subsystem to control the climber arms
 */
public class ClimberArms extends SubsystemBase implements Logger {

    // left NEO control classes
    private CANSparkMax leftArmMotor;
    private CANEncoder leftEncoder;
    private CANPIDController leftPIDController;

    // right NEO control classes
    private CANSparkMax rightArmMotor;
    private CANEncoder rightEncoder;
    private CANPIDController rightPIDController;

    // pneumatic piston control classes
    private Solenoid extendClimb;

    //TODO - Tune default pid values
    public static final double
    defaultP = 0,
    defaultI = 0,
    defaultD = 0;

    //TODO - Fix setpoints
    /**
     * Enum to provide preset target points for the climber motors to go to
     */
    public enum ClimbDefaults {
        STORED(0),
        RAISED(0),
        CLIMBING(0);

        public double setpoint;
        private ClimbDefaults(double setpoint) {
            this.setpoint = setpoint;
        }
    }

    public ClimbDefaults state;

    public ClimberArms() {
        leftArmMotor = new CANSparkMax(Ports.LEFT_ARM_MOTOR, MotorType.kBrushless);
        rightArmMotor = new CANSparkMax(Ports.RIGHT_ARM_MOTOR, MotorType.kBrushless);

        leftArmMotor.setInverted(true);
        rightArmMotor.setInverted(false);

        leftEncoder = leftArmMotor.getEncoder();
        rightEncoder = rightArmMotor.getEncoder();

        leftPIDController = leftArmMotor.getPIDController();
        rightPIDController = rightArmMotor.getPIDController();

        extendClimb = new Solenoid(Ports.PCM_1, Ports.EXTEND_CLIMB);

        resetPID();

        state = ClimbDefaults.STORED;
    }

    /**
     * Sets both motor's PID values simultaneously
     * 
     * @param P the new P value
     * @param I the new I value
     * @param D the new D value
     */
    public void setPID(double P, double I, double D) {
        setLeftPID(P, I, D);
        setRightPID(P, I, D);
    }

    /**
     * Sets the left motor's PID value
     * 
     * @param P the new P value
     * @param I the new I value
     * @param D the new D value
     */
    public void setLeftPID(double P, double I, double D) {
        leftPIDController.setP(P);
        leftPIDController.setI(I);
        leftPIDController.setD(D);
    }

    /**
     * Sets the right motor's PID value
     * 
     * @param P the new P value
     * @param I the new I value
     * @param D the new D value
     */
    public void setRightPID(double P, double I, double D) {
        rightPIDController.setP(P);
        rightPIDController.setI(I);
        rightPIDController.setD(D);
    }

    /**
     * Resets both motor's PID values to default constants
     */
    public void resetPID() {
        setLeftPID(defaultP, defaultI, defaultD);
        setRightPID(defaultP, defaultI, defaultD);
    }

    /**
     * Sets the pistons to expand away from the robot
     */
    public void extendClimb() {
        extendClimb.set(true);
    }

    public void releaseClimb() {
        extendClimb.set(false);
    }

    public void setLeftMotorPower(double power) {
        leftArmMotor.set(power);
    }

    public void setRightMotorPower(double power) {
        rightArmMotor.set(power);
    }

    public void setPower(double power) {
        setLeftMotorPower(power);
        setRightMotorPower(power);
    }

    /**
     * Sets the target position of the left climber motor
     * 
     * @param setpoint the new target for the motor
     */
    public void setLeftMotorSpool(double setpoint) {
        leftPIDController.setReference(setpoint, ControlType.kPosition);
    }

    /**
     * Sets the target position of the left climber motor
     * 
     * @param setpoint the new target for the motor
     */
    public void setRightMotorSpool(double setpoint) {
        rightPIDController.setReference(setpoint, ControlType.kPosition);
    }

    /**
     * Sets the climber motors to preset values
     * 
     * @param value the target ClimbDefault
     */
    public void setSpool(ClimbDefaults value) {
        setLeftMotorSpool(value.setpoint);
        setRightMotorSpool(value.setpoint);

        state = value;
    }

    /**
     * Logs climber motor positions
     */
    @Override
    public double[] getValues(double[] values) {
        values[LoggerRelations.CLIMB_LEFT_MOTOR.value] = leftEncoder.getPosition();
        values[LoggerRelations.CLIMB_RIGHT_MOTOR.value] = rightEncoder.getPosition();
        return values;
    }
}