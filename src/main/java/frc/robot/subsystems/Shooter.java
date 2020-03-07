package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;

/**
 * Subsystem to control the shooter
 */
public class Shooter extends SubsystemBase {

    private TalonSRX shooterMotor;
    private SensorCollection shooterEncoder;

    private CANSparkMax adjustableHood;
    private CANEncoder hoodEncoder;

    public Shooter() {
        shooterMotor = new TalonSRX(Ports.SHOOTER);
        shooterEncoder = shooterMotor.getSensorCollection();

        new VictorSPX(Ports.SHOOTER_FOLLOWER).follow(shooterMotor);

        adjustableHood = new CANSparkMax(Ports.ADJUSTABLE_HOOD, MotorType.kBrushless);
        hoodEncoder = adjustableHood.getEncoder();
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
    public double getRPM() {
        return shooterEncoder.getQuadratureVelocity();
    }
}