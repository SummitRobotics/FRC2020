package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.logging.Logger;
import frc.robot.utilities.Constants;

public class Drivetrain implements Subsystem, Logger {

    private double rightMotorPower, leftMotorPower;

    public Drivetrain() {
        rightMotorPower = 0;
        leftMotorPower = 0;
    }

    @Override
    public double[] getValues(double[] values) {
        values[Constants.LoggerRelations.LEFT_MOTOR.value] = leftMotorPower;
        values[Constants.LoggerRelations.RIGHT_MOTOR.value] = rightMotorPower;

        return values;
    }
}