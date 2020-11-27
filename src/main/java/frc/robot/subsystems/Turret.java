package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Homeable;
import frc.robot.utilities.Ports;

/**
 * Subsystem to control the turret
 */
public class Turret extends SubsystemBase implements Homeable {

    private CANSparkMax turret;
    private CANEncoder encoder;
    private CANPIDController pidController;


    public Turret() {
        turret = new CANSparkMax(Ports.TURRET, MotorType.kBrushless);
        encoder = turret.getEncoder();
        pidController = turret.getPIDController();

        turret.setClosedLoopRampRate(0);
        pidController.setOutputRange(-1, 1);

        turret.setInverted(false);
        turret.setIdleMode(IdleMode.kBrake);
    }


    /**
     * Sets power of the turret motor
     * 
     * @param power new power for the motor
     */
    public void setPower(double power) {
        power = Functions.clampDouble(power, 1, -1);
        turret.set(power);
    }

    /**
     * Sets encoder target for turret
     * 
     * @param value new setpoint for the motor
     */
    public void setTarget(double value) {
        pidController.setReference(value, ControlType.kPosition);
    }

    /**
     * Stops the turret motor
     */
    public void stop() {
        turret.set(0);
    }

    public double getEncoder(){
        return encoder.getPosition();
    }

    @Override
    public double getCurrent() {
        return turret.getOutputCurrent();
    }

    @Override
    public double getVelocity() {
        return encoder.getVelocity();
    }

    @Override
    public void setHomingPower(double power) {
        setPower(power);

    }

    @Override
    public void setHome(double position) {
        encoder.setPosition(position);

    }

    @Override
    public void setSoftLimits(double revers, double fowards) {
        turret.setSoftLimit(SoftLimitDirection.kReverse, (float)revers);
        turret.setSoftLimit(SoftLimitDirection.kForward, (float)fowards);
    }

    @Override
    public void DisableSoftLimits() {
        turret.enableSoftLimit(SoftLimitDirection.kForward, false);
        turret.enableSoftLimit(SoftLimitDirection.kReverse, false);

    }

    @Override
    public void EnableSoftLimits() {
        turret.enableSoftLimit(SoftLimitDirection.kForward, true);
        turret.enableSoftLimit(SoftLimitDirection.kReverse, true);
    }

    @Override
    public Subsystem getSubsystemObject() {
        return this;
    }
}