package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class Spool extends CommandBase {

	private Shooter shooter;

	public Spool(Shooter shooter) {
		this.shooter = shooter;

		addRequirements(shooter);
	}

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interupted) {

	}

	@Override
	public boolean isFinished() {
		return false;
	}
}