package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;
import frc.robot.utilities.RollingAverage;

/**
 * Subsystem to control the shooter
 */
public class Shooter extends SubsystemBase {

    //TODO - make good
    public static final double SPOOL_POWER = 1;

    //TODO - tune spooled velocity
    private final static double SPOOLED_VELOCITY = 12;

    private CANSparkMax shooterMotor;
    private CANEncoder shooterEncoder;

    private RollingAverage average;

    public Shooter() {
        shooterMotor = new CANSparkMax(Ports.SHOOTER, MotorType.kBrushless);
        shooterEncoder = shooterMotor.getEncoder();

        shooterMotor.setClosedLoopRampRate(0);

        average = new RollingAverage(10);
    }

    /**
     * Sets the shooter power
     * 
     * @param power the new power
     */
    public void setPower(double power) {
        power = Functions.clampDouble(power, 1, -1);
        shooterMotor.set(power);
    }

    /**
     * Stops the motor
     */
    public void stop() {
        shooterMotor.set(0);
    }

    /**
     * Gets the velocity of the shooter
     * 
     * @return the velocity
     */
    public double getRPM() {
        return shooterEncoder.getVelocity();
    }

    /**
     * Checks if the shooter is it at its spooled point
     * 
     * @return if it's spooled
     */
    public boolean spooled() {
        return average.getAverage() >= SPOOLED_VELOCITY; 
    }

    @Override
    public void periodic() {
        average.update(getRPM());
    }
}