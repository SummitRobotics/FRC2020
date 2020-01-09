package frc.robot.devices;

import com.ctre.phoenix.sensors.PigeonIMU;
import frc.robot.utilities.Constants.LoggerRelations;
import frc.robot.logging.Logger;

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

    public double getXGForce() {
        short[] axies = {0,0,0};
        gyro.getBiasedAccelerometer(axies);
        double out = (((double) axies[0]) / 16384.0);
        return out;
    }

    public double getYAngle() {
        double[] angles = {0,0,0};
        gyro.getYawPitchRoll(angles);
        double out = angles[2];
        return out;
    }

    public double getHeading() {
        return gyro.getFusedHeading();
    }

    public void setHeading(double setpoint) {
        gyro.setFusedHeading(setpoint);
    }
}