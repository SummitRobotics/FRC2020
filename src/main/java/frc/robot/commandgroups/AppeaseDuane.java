package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.EncoderMovement;
import frc.robot.commands.GyroTurn;
import frc.robot.devices.PigeonGyro;
import frc.robot.subsystems.Drivetrain;

public class AppeaseDuane extends SequentialCommandGroup {

    public AppeaseDuane(Drivetrain drivetrain, PigeonGyro gyro) {
        addCommands(
            new EncoderMovement(drivetrain, 60, 60),
            new GyroTurn(gyro, drivetrain, 90),
            new EncoderMovement(drivetrain, 60, 60),
            new GyroTurn(gyro, drivetrain, -90),
            new EncoderMovement(drivetrain, -60, -60),
            new GyroTurn(gyro, drivetrain, 90),
            new EncoderMovement(drivetrain, -60, -60),
            new GyroTurn(gyro, drivetrain, -90),
            new EncoderMovement(drivetrain, 100, 100)
        );
    }
}