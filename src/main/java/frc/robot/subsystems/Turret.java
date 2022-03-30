package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Homeable;
import frc.robot.utilities.lists.Ports;
import frc.robot.oi.shufhellboardwidgets.DoubleDisplayWidget;

/**
 * Subsystem to control the turret
 */
public class Turret extends SubsystemBase implements Homeable {

    private CANSparkMax turret;
    private RelativeEncoder encoder;
    private DoubleDisplayWidget indicator;

    public static final double MAX_CHANGE_RATE = 0.025;

    public static final double fowardLimit = 27;
    public static final double shootingBackLimit = 7;
    public static final double normalBackLimit = 2;

    public Turret(DoubleDisplayWidget indicator) {
        this.indicator = indicator;

        this.turret = new CANSparkMax(Ports.TURRET, MotorType.kBrushless);
        this.encoder = turret.getEncoder();

        turret.setInverted(true);
        turret.setSmartCurrentLimit(30);
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
     * Stops the turret motor
     */
    public void stop() {
        turret.set(0);
    }

    public double getEncoder() {
        return encoder.getPosition();
    }

    public double getAngle() {
        return encoderToAngle(getEncoder());
    }

    public static double encoderToAngle(double encoder){
        double out = encoder*360;
        out = out/69;
        return out;
    }

    public static double angleToEncoder(double angle){
        double out = angle*69;
        out = out/360;
        return out;
    }

    public boolean isAtFowardLimit() {
        return turret.isSoftLimitEnabled(SoftLimitDirection.kForward) && (turret.getSoftLimit(SoftLimitDirection.kForward) >= getEncoder());
    }

    public boolean isAtReverseLimit() {
        return turret.isSoftLimitEnabled(SoftLimitDirection.kForward) && (turret.getSoftLimit(SoftLimitDirection.kForward) >= getEncoder());
    }

    public boolean isAtLimit() {
        return isAtFowardLimit() || isAtReverseLimit();
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

    @Override
    public void periodic() {
        indicator.setValue(getAngle());
    }
}