package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberArm;
import frc.robot.utilities.Functions;

public class MoveArm extends CommandBase {

	private ClimberArm arm;

	private int distance;
	private boolean end;

	public MoveArm(ClimberArm arm, int distance) {
		this.arm = arm;
		this.distance = distance;

		end = false;

		addRequirements(arm);
	}

	@Override
	public void execute() {
		double currentPosition = arm.getEncoderPosition();

		if (Functions.isWithin(currentPosition, distance, 10)) {
			arm.setPower(0);
			end = true;
		} else {
			arm.setPower(Math.copySign(0.5, distance - currentPosition));
		}
	}

	@Override
	public void end(boolean interrupted) {
		arm.stop();
	}

	@Override
	public boolean isFinished() {
		return end;
	}
}