package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.utilities.Ports;

public class ClimberArm extends SubsystemBase {

    public enum Sides {
        LEFT(Ports.LEFT_ARM_MOTOR, true),
        RIGHT(Ports.RIGHT_ARM_MOTOR, false);

        public int motorPort;
        public boolean inverted;
        private Sides(int motorPort, boolean inverted) {
            this.motorPort = motorPort;
            this.inverted = inverted;
        }
    }

    public static final int 
    LEFT_CONTROL_PANEL_POSITION = 205,
    CLIMB_POSITION = 615,
    LIFT_POSITION = -400; //TODO - fix

    private CANSparkMax motor;
    private CANEncoder encoder;
    private CANPIDController pidController;

    private static final double
    defaultP = 0,
    defaultI = 0,
    defaultD = 0;
    
    public ClimberArm(Sides side) {
        motor = new CANSparkMax(side.motorPort, MotorType.kBrushless);
        motor.setInverted(side.inverted);

        encoder = motor.getEncoder();

        pidController = motor.getPIDController();
        resetPID();
    }

    public double getEncoderPosition() {
        return encoder.getPosition();
    }

    public void setEncoderPosition(double position) {
        encoder.setPosition(position);
    }

    public void setPID(double P, double I, double D) {
        pidController.setP(P);
        pidController.setI(I);
        pidController.setD(D);
    }

    public void resetPID() {
        setPID(defaultP, defaultI, defaultD);
    }

    public void setPower(double power) {
        motor.set(power);
    }

    public void stop() {
        setPower(0);
    }

    public void setPosition(double setpoint) {
        pidController.setReference(setpoint, ControlType.kPosition);
    }
}