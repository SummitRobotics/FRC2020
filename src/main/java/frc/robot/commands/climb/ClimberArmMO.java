package frc.robot.commands.climb;

import frc.robot.commandegment.CommandBase;
import frc.robot.oi.inputs.OIAxis;
import frc.robot.subsystems.ClimberArm;
import frc.robot.utilities.lists.commandPriorities;

public class ClimberArmMO extends CommandBase {

	private ClimberArm arm;
	private OIAxis controlAxis;

	/**
	 * Command for manually controlling a single climber arm
	 */
	public ClimberArmMO(ClimberArm arm, OIAxis controlAxis) {
		addRequirements(arm);
		setPriority(commandPriorities.MoPriority);

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
		//System.out.println(arm.getside() + " arm encoder: " + arm.getEncoderPosition());
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