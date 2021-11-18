package frc.robot.commands.climb;

import frc.robot.commandegment.CommandBase;
import frc.robot.subsystems.ClimberArm;

public class LowerArm extends CommandBase {

	private ClimberArm arm;
	private int distance;

	/**
	 * Command to lower a single climber arm a specific distance
	 * @param arm the climber arm
	 * @param distance the distance to travel
	 */
	public LowerArm(ClimberArm arm, int distance) {
		this.arm = arm;
		this.distance = distance;

		addRequirements(arm);
	}

	@Override
	public void initialize() {
		arm.setEncoderPosition(0);
		arm.setPower(-1);
	}

	@Override
	public void end(boolean interrupted) {
		arm.stop();
	}

	@Override
	public boolean isFinished() {
		return arm.getEncoderPosition() <= distance;
	}
}