package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.oi.LEDButton.LED;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberPneumatics;

//TODO - test on robot
public class ClimbSequence extends SequentialCommandGroup {

	/**
	 * Robot's climbing sequence
	 * 
	 * First, arms are pressurized and raised to a target setpoint. Then, trim sliders are activated and the driver moves the robot into position.
	 * Finally, the missile switch is flipped off, and the arms depressurize.
	 * 
	 * @param leftArm the left climber arm
	 * @param rightArm the right climber arm
	 * @param pneumatics the climber pneumatic controller
	 * @param leftSlider the slider on the lauchpad used to tune the left arm
	 * @param rightSlider the slider on the lauchpad used to tune the right arm
	 * @param nextPhase the button that starts the climb, and on release, ends it
	 * @param ledA one half of the big LED on the launchpad
	 * @param ledB the other half of the big LED on the launchpad
	 */
	public ClimbSequence(
		ClimberArm leftArm, 
		ClimberArm rightArm, 
		ClimberPneumatics pneumatics,
		LoggerAxis leftSlider, 
		LoggerAxis rightSlider,
		LoggerButton nextPhase,
		LED ledA,
		LED ledB
	) {

		Command trim = new ParallelCommandGroup(
			new TrimArm(leftArm, leftSlider),
			new TrimArm(rightArm, rightSlider)
		) {
			@Override
			public boolean isFinished() {
				return super.isFinished() || !nextPhase.get();
			}

			@Override
			public void end(boolean interrupted) {
				super.end(interrupted);

				if (!interrupted) {
					pneumatics.retractClimb();
				}
			}
		};

		addCommands(
			new InstantCommand(pneumatics::extendClimb),
			new InstantCommand(() -> {
				ledA.set(false);
				ledB.set(true);
			}),
			new RaiseArmsSync(leftArm, rightArm, ClimberArm.CLIMB_POSITION),
			trim
		);
	}
}