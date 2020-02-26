package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.oi.LEDButton.LED;
import frc.robot.subsystems.ClimberArm;
import frc.robot.utilities.Functions;

public class ClimbSequence extends SequentialCommandGroup {

	public ClimbSequence(
		ClimberArm leftArm, 
		ClimberArm rightArm, 
		LoggerAxis leftSlider, 
		LoggerAxis rightSlider,
		LoggerButton nextPhase,
		LED ledA,
		LED ledB
	) {

		Command trim = new ParallelCommandGroup(
			new TrimArm(leftArm, leftSlider),
			new TrimArm(rightArm, rightSlider)
		);

		Functions.bindCommand(
			trim, 
			nextPhase, 
			Trigger::whenInactive,
			new SequentialCommandGroup(
				new InstantCommand(() -> ledB.set(true)),
				new RaiseArmsSync(leftArm, rightArm, ClimberArm.LIFT_POSITION)
			)
		);

		addCommands(
			new InstantCommand(() -> ledA.set(true)),
			new RaiseArmsSync(leftArm, rightArm, ClimberArm.CLIMB_POSITION),
			trim
		);

		addCommands();
	}
}