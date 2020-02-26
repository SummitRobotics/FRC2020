package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.oi.LoggerAxis;
import frc.robot.subsystems.ClimberArm;
import frc.robot.utilities.MOCommand;

public class ClimberArmMO extends MOCommand {

	private ClimberArm arm;
	private LoggerAxis controlAxis;

	public ClimberArmMO(Subsystem controlSystem, ClimberArm arm, LoggerAxis controlAxis) {
		super(controlSystem, arm);

		this.arm = arm;
		this.controlAxis = controlAxis;
	}

	@Override
	public void initialize() {
		arm.stop();
	}

	@Override
	public void execute() {
		arm.setPower(controlAxis.get());
	}

	@Override
	public void end(boolean interrupted) {
		arm.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}