package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberArm;

public class MoveArmsSync extends ParallelCommandGroup {

	public MoveArmsSync(ClimberArm armA, ClimberArm armB, int distance) {
		addCommands(
			new MoveArm(armA, distance),
			new MoveArm(armB, distance)
		);
	}
}