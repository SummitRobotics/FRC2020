package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberArm;

public class RaiseArmsSync extends ParallelCommandGroup {

	private ClimberArm leftArm, rightArm;

	private int leftDistance = 332;
	private int rightDistance = 310;

	/**
	 * Raises both climber arms a specific distance
	 * @param leftArm the left climber arm
	 * @param rightArm the right climber arm
	 */
	public RaiseArmsSync(ClimberArm leftArm, ClimberArm rightArm) {

		this.leftArm = leftArm;
		this.rightArm = rightArm;

		addCommands(
			new RaiseArm(leftArm, leftDistance),
			new RaiseArm(rightArm, rightDistance)
		);
	}
}