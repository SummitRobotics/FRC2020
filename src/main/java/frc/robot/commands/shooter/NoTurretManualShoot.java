package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.shooter.Spool;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.utilities.MOCommand;

/**
 * Command for running the full manual mode
 */
public class NoTurretManualShoot extends MOCommand {

	private Command 
	expel,
	spool;

	public NoTurretManualShoot 
		(
			Subsystem controlSystem,
			Shooter shooter, 
			Conveyor conveyor, 
			LoggerButton trigger,
			LoggerButton upper
		) {

		super(controlSystem);

		expel = bindCommand(trigger, Trigger::whileActiveContinuous, conveyor.toggleShootMode);
		spool = bindCommand(upper, Trigger::whileActiveContinuous, new Spool(shooter));
	}

	@Override
	public void end(boolean interupted) {
		expel.cancel();
		spool.cancel();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}