package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.ProxyScheduleCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.oi.LEDButton.LED;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberPneumatics;
import frc.robot.utilities.Functions;

public class ClimbSequence extends SequentialCommandGroup {

	public ClimbSequence(
		ClimberArm leftArm, 
		ClimberArm rightArm, 
		ClimberPneumatics pneumatics,
		LoggerAxis leftSlider, 
		LoggerAxis rightSlider,
		LoggerAxis controlAxis,
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
				new InstantCommand(() -> {
					ledA.set(true);
					ledB.set(false);
				}),
				new InstantCommand(pneumatics::retractClimb)
			)
		);

		addCommands(
			new InstantCommand(pneumatics::extendClimb),
			new InstantCommand(() -> {
				ledA.set(false);
				ledB.set(true);
			}),
			new RaiseArmsSync(leftArm, rightArm, ClimberArm.CLIMB_POSITION),
			new ProxyScheduleCommand(trim)
		);
	}
}