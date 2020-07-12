package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberArm;

public class RaiseArmsSync extends ParallelCommandGroup {

	private ClimberArm leftArm, rightArm;

	private int leftDistance = 325;
	private int rightDistance = 315;

	/**
	 * Raises both climber arms a specific distance
	 * @param leftArm the left climber arm
	 * @param rightArm the right climber arm
	 * @param distance the distance
	 */
	public RaiseArmsSync(ClimberArm leftArm, ClimberArm rightArm) {

		this.leftArm = leftArm;
		this.rightArm = rightArm;

		addCommands(
			new RaiseArm(leftArm, leftDistance),
			new RaiseArm(rightArm, rightDistance)
		);
	}

	@Override
	public void execute() {
		super.execute();

		SmartDashboard.putNumber("Left Arm", leftArm.getEncoderPosition());
		SmartDashboard.putNumber("Right Arm", rightArm.getEncoderPosition());
	}
}