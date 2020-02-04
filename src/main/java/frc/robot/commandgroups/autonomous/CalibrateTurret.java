package frc.robot.commandgroups.autonomous;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.turret.RunToLimitSwitch;
import frc.robot.subsystems.Turret;

public class CalibrateTurret extends SequentialCommandGroup {

    public CalibrateTurret(Turret turret) {
        addCommands(
            new RunToLimitSwitch(turret),
            new InstantCommand(turret::calibrateEncoder)
        );
    }
}