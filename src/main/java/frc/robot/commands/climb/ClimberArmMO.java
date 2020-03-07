package frc.robot.commands.climb;

import frc.robot.oi.LoggerAxis;
import frc.robot.subsystems.ClimberArm;
import frc.robot.utilities.MOCommand;

public class ClimberArmMO extends MOCommand {

	private ClimberArm arm;
	private LoggerAxis controlAxis;

	public ClimberArmMO(ClimberArm arm, LoggerAxis controlAxis) {
		addRequirements(arm);

		this.arm = arm;
		this.controlAxis = controlAxis;
	}

	@Override
	public void initialize() {
		super.initialize();
		arm.stop();
	}

	@Override
	public void execute() {
		arm.setPower(controlAxis.get());
	}

	@Override
	public void end(boolean interrupted) {
		super.end(interrupted);
		arm.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}