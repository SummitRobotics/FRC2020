package frc.robot.commands.shooter;

import frc.robot.commandegment.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * Command to spool the shooter
 */
public class Spool extends CommandBase {

	private Shooter shooter;
	private double power;

	public Spool(Shooter shooter, double power) {
		this.shooter = shooter;
		this.power = power;

		addRequirements(shooter);
	}

	@Override
	public void execute() {
		shooter.setPower(power);
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