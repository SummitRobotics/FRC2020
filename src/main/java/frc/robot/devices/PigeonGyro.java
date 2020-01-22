package frc.robot.devices;

import com.ctre.phoenix.sensors.PigeonIMU;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;

public class PigeonGyro implements Logger {

    private PigeonIMU gyro;

    public PigeonGyro(int port) {
        gyro = new PigeonIMU(port);
    }

    @Override
    public double[] getValues(double[] values) {

        values[LoggerRelations.IMU_X_GFORCE.value] = getXGForce();
        values[LoggerRelations.IMU_Y_ANGLE.value] = getYAngle();
        values[LoggerRelations.IMU_HEADING.value] = getHeading();

        return values;
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

    /**
     * makes the curent heading of the robot read as the argument
     * @param setpoint the heading you want the current posision to be
     */
    public void setHeading(double setpoint) {
        gyro.setFusedHeading(setpoint);
    }
}