package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
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
    private CANSparkMax shooterMotor2;
    //private CANEncoder shooterEncoder;

    private RollingAverage average;

    public Shooter() {
        shooterMotor = new CANSparkMax(Ports.SHOOTER, MotorType.kBrushed);
        shooterMotor2 = new CANSparkMax(Ports.SHOOTER1, MotorType.kBrushed);
        //shooterEncoder = shooterMotor.getEncoder();

        shooterMotor2.follow(shooterMotor);
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
        System.out.println(power);
    }

    /**
     * Stops the motor
     */
    public void stop() {
        shooterMotor.set(0);
        System.out.println("stoped");
    }

    /**
     * Gets the velocity of the shooter
     * 
     * @return the velocity
     */
    public double getRPM() {
        return 0;//shooterEncoder.getVelocity();
    }

    /**
     * Checks if the shooter is it at its spooled point
     * 
     * @return if it's spooled
     */
    public boolean spooled() {
        return false;//average.getAverage() >= SPOOLED_VELOCITY; 
    }

    @Override
    public void periodic() {
        //average.update(getRPM());
    }
}