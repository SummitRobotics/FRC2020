package frc.robot.devices;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

//TODO - gut it

/**
 * Device to run gyro
 */
public class PigeonGyro {

    private PigeonIMU gyro;

    public PigeonGyro(TalonSRX motor) {
        gyro = new PigeonIMU(motor);
    }

    /**
     * gets the force expreinced by the robots x axis (parallel whith the drivetrain)
     * @return force in g's
     */
    public double getXGForce() {
        short[] axies = {0,0,0};
        gyro.getBiasedAccelerometer(axies);
        double out = (((double) axies[0]) / 16384.0);
        return out;
    }

    /**
     * gets the fowards and backwards tilit of the robot
     * @return the tilt angle
     */
    public double getYAngle() {
        double[] angles = {0,0,0};
        gyro.getYawPitchRoll(angles);
        double out = angles[2];
        return out;
    }

    /**
     * gets the heading of the robot
     * @return heading angle, not limited to 180
     */
    public double getHeading() {
        return gyro.getFusedHeading();
    }

    public double getCompas(){
        return gyro.getCompassHeading();
    }

    /**
     * makes the curent heading of the robot read as the argument
     * @param setpoint the heading you want the current posision to be
     */
    public void setHeading(double setpoint) {
        gyro.setFusedHeading(setpoint);
    }
}