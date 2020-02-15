package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.subsystems.Shooter;

/**
 * Command to spool when the turret is on target
 */
public class SpoolOnTarget extends CommandBase {

	//TODO - make right
	private static final double TARGET_CLOSE_CUTOFF = 1;

	private Shooter shooter;
	private Lemonlight limelight;

	public SpoolOnTarget(Shooter shooter, Lemonlight limelight) {
		this.shooter = shooter;
		this.limelight = limelight;

		addRequirements(shooter);
	}

	@Override
	public void execute() {
		if (limelight.getHorizontalOffset() < TARGET_CLOSE_CUTOFF) {
			shooter.setPower(Shooter.SPOOL_POWER);
		} else {
			shooter.setPower(0);
		}
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