package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.utilities.lists.Ports;

public class ClimberArm extends SubsystemBase {

    public enum Sides {
        LEFT(Ports.LEFT_CLIMB, true),
        RIGHT(Ports.RIGHT_CLIMB, false);

        public int motorPort;
        public boolean inverted;

        private Sides(int motorPort, boolean inverted) {
            this.motorPort = motorPort;
            this.inverted = inverted;
        }
    }
 
    private CANSparkMax motor;
    private CANEncoder encoder;
    private CANPIDController pidController;
    private Sides side;

    private static final double
    defaultP = 0,
    defaultI = 0,
    defaultD = 0;
    
    public ClimberArm(Sides side) {
        this.side = side;
        motor = new CANSparkMax(side.motorPort, MotorType.kBrushless);
        motor.setInverted(side.inverted);
        motor.setIdleMode(IdleMode.kBrake);

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

    public String getside(){
        return side.toString();
    }
}