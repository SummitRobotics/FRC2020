package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberArm;

public class RaiseArmsSync extends ParallelCommandGroup {

	public RaiseArmsSync(ClimberArm leftArm, ClimberArm rightArm, int distance) {
		addCommands(
			new RaiseArm(leftArm, distance),
			new RaiseArm(leftArm, distance)
		);
	}
}