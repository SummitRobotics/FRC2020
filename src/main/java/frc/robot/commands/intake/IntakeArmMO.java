package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.IntakeArm;

public class IntakeArmMO extends SequentialCommandGroup {

	private IntakeArm intakeArm;

	public IntakeArmMO(Subsystem controlSystem, IntakeArm intakeArm, LoggerAxis controlAxis, LoggerButton controlButtonA, LoggerButton controlButtonB) {
		this.intakeArm = intakeArm;

		addCommands(
			new InstantCommand(this::cancelIntakeCommand),
			new IntakeArmMOProxy(controlSystem, intakeArm, controlAxis, controlButtonA, controlButtonB)
		);
	}

	private void cancelIntakeCommand() {
		Command potentialProblem = CommandScheduler.getInstance().requiring(intakeArm);

		if (potentialProblem != null) {
			potentialProblem.cancel();
		}
	}
}