package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.shooter.SpoolOnTarget;
import frc.robot.devices.Lemonlight;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.Functions;

/**
 * Command for running the semi auto mode
 */
public class SemiAutoShooterAssembly extends ParallelRaceGroup {

	public SemiAutoShooterAssembly
		(
			Turret turret, 
			Shooter shooter, 
			Conveyor conveyor,
			Lemonlight limelight, 
			LoggerButton trigger
		) {
		
		Functions.bindCommand(this, trigger, Trigger::whileActiveOnce, conveyor.toggleSafeShootMode);

		addCommands(
			new SpoolOnTarget(shooter, limelight),
			new VisionTarget(turret, limelight)
		);
	}
}