package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.ClimberArm;

public class RaiseArmsSync extends ParallelCommandGroup {

	private ClimberArm leftArm, rightArm;

	/**
	 * Raises both climber arms a specific distance
	 * @param leftArm the left climber arm
	 * @param rightArm the right climber arm
	 * @param distance the distance
	 */
	public RaiseArmsSync(ClimberArm leftArm, ClimberArm rightArm, int distance) {

		this.leftArm = leftArm;
		this.rightArm = rightArm;

		addCommands(
			new RaiseArm(leftArm, distance),
			new RaiseArm(rightArm, distance)
		);
	}

	@Override
	public void execute() {
		super.execute();

		SmartDashboard.putNumber("Left Arm", leftArm.getEncoderPosition());
		SmartDashboard.putNumber("Right Arm", rightArm.getEncoderPosition());
	}
}