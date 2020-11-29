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
public class Shooter extends SubsystemBase {

    private TalonFX shooterMotor;
    private TalonFXSensorCollection shooterEncoder;

   

    public Shooter() {
        shooterMotor = new TalonFX(Ports.SHOOTER);
        shooterEncoder = new TalonFXSensorCollection(shooterMotor);

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

   
}