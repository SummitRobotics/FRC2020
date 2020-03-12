package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.commands.shooter.SpoolOnTarget;
import frc.robot.devices.Lemonlight;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

/**
 * Command for running the full auto mode
 */
public class FullAutoShooterAssembly extends ParallelRaceGroup {

	public FullAutoShooterAssembly(Turret turret, Shooter shooter, Conveyor conveyor, Lemonlight limelight) {
		addCommands(
			new VisionTarget(turret, limelight),
			new SpoolOnTarget(shooter, limelight)
		);
	}
}