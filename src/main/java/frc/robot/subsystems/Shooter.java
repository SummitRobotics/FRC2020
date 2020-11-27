package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Homeable;
import frc.robot.utilities.Ports;

/**
 * Subsystem to control the shooter
 */
public class Shooter extends SubsystemBase implements Homeable {

    private TalonFX shooterMotor;
    private TalonFXSensorCollection shooterEncoder;

    private CANSparkMax adjustableHood;
    private CANEncoder hoodEncoder;

    public Shooter() {
        shooterMotor = new TalonFX(Ports.SHOOTER);
        shooterEncoder = new TalonFXSensorCollection(shooterMotor);

        adjustableHood = new CANSparkMax(Ports.ADJUSTABLE_HOOD, MotorType.kBrushless);

        hoodEncoder = adjustableHood.getAlternateEncoder();
        hoodEncoder.setPosition(0);
        adjustableHood.setSoftLimit(SoftLimitDirection.kForward, (float)11.5);
    }

    /**
     * Sets the shooter power
     * @param power the new power
     */
    public void setPower(double power) {
        power = Functions.clampDouble(power, 1, -1);
        shooterMotor.set(ControlMode.PercentOutput, power);
    }

    /**
     * Stops the motor
     */
    public void stop() {
        shooterMotor.set(ControlMode.PercentOutput, 0);
        adjustableHood.set(0);
    }

    public void setHoodPower(double power) {
        power = Functions.clampDouble(power, 1, -1);
        adjustableHood.set(power);
    }

    // TODO - convert to RPM
    /**
     * Gets the velocity of the shooter
     * @return the velocity
     */
    public double getShooterRPM() {
        return shooterEncoder.getIntegratedSensorVelocity();
    }

    public double getShooterTemperature() {
        return shooterMotor.getTemperature();
    }

    public double getShooterCurrentDraw() {
        return shooterMotor.getSupplyCurrent();
    }

    //for homing adj hood
    @Override
    public double getCurrent() {
        return adjustableHood.getOutputCurrent();
    }

    @Override
    public double getVelocity() {
        return hoodEncoder.getVelocity();
    }

    @Override
    public void setHomingPower(double power) {
        setHoodPower(power);

    }

    @Override
    public void setHome(double position) {
        hoodEncoder.setPosition(position);
    }

    @Override
    public void setSoftLimits(double revers, double fowards) {
        //enables both soft limits
        adjustableHood.setSoftLimit(SoftLimitDirection.kForward, (float)fowards);
        adjustableHood.setSoftLimit(SoftLimitDirection.kReverse, (float)revers);

    }

    @Override
    public void DisableSoftLimits() {
        adjustableHood.enableSoftLimit(SoftLimitDirection.kForward, false);
        adjustableHood.enableSoftLimit(SoftLimitDirection.kReverse, false);

    }

    @Override
    public void EnableSoftLimits() {
        adjustableHood.enableSoftLimit(SoftLimitDirection.kForward, true);
        adjustableHood.enableSoftLimit(SoftLimitDirection.kReverse, true);

    }

    @Override
    public Subsystem getSubsystemObject(){
        return this;
    }
}