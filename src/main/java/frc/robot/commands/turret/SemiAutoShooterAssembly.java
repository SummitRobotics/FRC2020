package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.shooter.SpoolOnTarget;
import frc.robot.devices.Lemonlight;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.MOCommand;

public class SemiAutoShooterAssembly extends ParallelRaceGroup {

	private MOCommand expel;

	public SemiAutoShooterAssembly
		(
			Turret turret, 
			Shooter shooter, 
			Conveyor conveyor,
			Lemonlight limelight, 
			LoggerButton trigger
		) {

		super();
		
		expel = new MOCommand(turret);
		expel.bindCommand(trigger, Trigger::whileActiveOnce, conveyor.toggleSafeShootMode);

		addCommands(
			expel,
			new SpoolOnTarget(shooter, limelight),
			new VisionTarget(turret, limelight)
		);
	}
}