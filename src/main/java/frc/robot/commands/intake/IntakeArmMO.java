package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.IntakeArm;

public class IntakeArmMO extends SequentialCommandGroup {

	private IntakeArm intakeArm;

	public IntakeArmMO(IntakeArm intakeArm, LoggerAxis controlAxis, LoggerButton controlButton) {
		this.intakeArm = intakeArm;

		addCommands(
			new InstantCommand(this::cancelIntakeCommand),
			new IntakeArmMOProxy(intakeArm, controlAxis, controlButton)
		);
	}

	private void cancelIntakeCommand() {
		Command potentialProblem = CommandScheduler.getInstance().requiring(intakeArm);

		if (potentialProblem != null) {
			potentialProblem.cancel();
		}
	}
}