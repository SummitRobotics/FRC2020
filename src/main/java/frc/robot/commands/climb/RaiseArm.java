package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberArm;

public class RaiseArm extends CommandBase {

	private ClimberArm arm;
	private int distance;

	public RaiseArm(ClimberArm arm, int distance) {
		this.arm = arm;
		this.distance = distance;

		addRequirements(arm);
	}

	@Override
	public void initialize() {
		arm.stop();
		arm.setEncoderPosition(0);
	}

	@Override
	public void end(boolean interrupted) {
		arm.stop();
	}

	@Override
	public boolean isFinished() {
		return arm.getEncoderPosition() > distance;
	}
}