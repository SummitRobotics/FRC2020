package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.ClimberArm;

public class LowerArmSync extends ParallelCommandGroup {

	/**
	 * Lowers both climber arms the same distance
	 * @param leftArm the left climber arm
	 * @param rightArm the right climber arm
	 * @param distance the distance to travel
	 */
	public LowerArmSync(ClimberArm leftArm, ClimberArm rightArm, int distance) {
		addCommands(
			new LowerArm(leftArm, distance),
			new LowerArm(rightArm, distance),

			//Outputs the climber arm heights
			new RunCommand(
				() -> System.out.print(leftArm.getEncoderPosition() + ", " + rightArm.getEncoderPosition())
			)
		);
	}
}