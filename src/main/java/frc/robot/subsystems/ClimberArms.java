package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
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
    private DoubleSolenoid leftPiston;
    private DoubleSolenoid rightPiston;

    //TODO - Tune PID values
    private static final double
    P = 0,
    I = 0,
    D = 0;

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

    public ClimberArms() {
        leftArmMotor = new CANSparkMax(Ports.LEFT_ARM_MOTOR.port, MotorType.kBrushless);
        leftEncoder = leftArmMotor.getEncoder();
        leftPIDController = leftArmMotor.getPIDController();

        rightArmMotor = new CANSparkMax(Ports.RIGHT_ARM_MOTOR.port, MotorType.kBrushless);
        leftEncoder = leftArmMotor.getEncoder();
        leftPIDController = leftArmMotor.getPIDController();

        leftPiston = new DoubleSolenoid(Ports.LEFT_PISTON_OPEN.port, Ports.LEFT_PISTON_CLOSE.port);
        rightPiston = new DoubleSolenoid(Ports.RIGHT_PISTON_OPEN.port, Ports.RIGHT_PISTON_CLOSE.port);

        leftArmMotor.setClosedLoopRampRate(0);
        rightArmMotor.setClosedLoopRampRate(0);

        leftPIDController.setP(P);
        leftPIDController.setI(I);
        leftPIDController.setD(D);

        rightPIDController.setP(P);
        rightPIDController.setI(I);
        rightPIDController.setD(D);
    }

    /**
     * Sets the pistons to expand away from the robot
     */
    public void openPistons() {
        leftPiston.set(Value.kForward);
        rightPiston.set(Value.kForward);
    }

    /**
     * Sets the pistons to retract towards the robot
     */
    public void closePistons() {
        leftPiston.set(Value.kReverse);
        rightPiston.set(Value.kReverse);
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