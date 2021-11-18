package frc.robot.commands.climb;

import frc.robot.commandegment.ParallelCommandGroup;
import frc.robot.subsystems.ClimberArm;

public class RaiseArmsSync extends ParallelCommandGroup {

	private static final int leftDistance = 332;
	private static final int rightDistance = 310;

	/**
	 * Raises both climber arms a specific distance
	 * @param leftArm the left climber arm
	 * @param rightArm the right climber arm
	 */
	public RaiseArmsSync(ClimberArm leftArm, ClimberArm rightArm) {
        super(
            new RaiseArm(leftArm, leftDistance),
			new RaiseArm(rightArm, rightDistance)
        );
	}
}