package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * Command to spool the shooter
 */
public class Spool extends CommandBase {

	private Shooter shooter;

	public Spool(Shooter shooter) {
		this.shooter = shooter;

		addRequirements(shooter);
	}

	@Override
	public void execute() {
		shooter.setPower(Shooter.SPOOL_POWER);
	}

	@Override
	public void end(boolean interupted) {
		shooter.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}