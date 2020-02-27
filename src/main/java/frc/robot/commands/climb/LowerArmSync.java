package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberArm;

public class LowerArmSync extends ParallelCommandGroup {

	public LowerArmSync(ClimberArm leftArm, ClimberArm rightArm, int distance) {
		addCommands(
			new LowerArm(leftArm, distance),
			new LowerArm(rightArm, distance)
		);
	}
}