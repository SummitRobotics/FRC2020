package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.ClimberArm;

public class RaiseArmsSync extends ParallelCommandGroup {

	private ClimberArm leftArm, rightArm;

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