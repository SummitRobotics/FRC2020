package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.oi.OIAxis;
import frc.robot.oi.OIButton;
import frc.robot.oi.LEDButton.LED;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberPneumatics;
import frc.robot.utilities.Colors;

public class ClimbSequence extends SequentialCommandGroup {

	/**
	 * Robot's climbing sequence
	 * 
	 * First, arms are pressurized and raised to a target setpoint. Then, trim 
	 * sliders are activated and the driver moves the robot into position.
	 * Finally, the missile switch is flipped off, and the arms depressurize.
	 * 
	 * @param leftArm the left climber arm
	 * @param rightArm the right climber arm
	 * @param pneumatics the climber pneumatic controller
	 * @param leftSlider the slider on the lauchpad used to tune the left arm
	 * @param rightSlider the slider on the lauchpad used to tune the right arm
	 * @param nextPhase the button that starts the climb, and on release, ends it
	 * @param ledA one half of the big LED on the launchpad
	 * @param ledB the other half of the big LED on the 
	 * @param leds the robot leds to control
	 */
	public ClimbSequence(
		ClimberArm leftArm, 
		ClimberArm rightArm, 
		ClimberPneumatics pneumatics,
		OIAxis leftSlider, 
		OIAxis rightSlider,
		OIButton nextPhase,
		LED ledA,
		LED ledB,
		LEDs leds
	) {


		/**
		 * A command to allow for both climber arms to be manually trimmed using the trimming sliders
		 * on the launchpad.
		 */
		Command trim = new ParallelCommandGroup(
			new TrimArm(leftArm, leftSlider),
			new TrimArm(rightArm, rightSlider)
		) {

			/**
			 * Ends the command when the missile latch is pushed down
			 */
			@Override
			public boolean isFinished() {
				return super.isFinished() || !nextPhase.get();
			}

			/**
			 * Depressurizes the arms if the trim ended properly, otherwise it leaves them up
			 */
			@Override
			public void end(boolean interrupted) {
				super.end(interrupted);

				leds.removeCall("ArmsUp");

				if (!interrupted) {
					pneumatics.retractClimb();
				}
			}
		};

		addCommands(
			new InstantCommand(() -> leds.addCall("ArmsUp", new LEDCall(7, LEDRange.BothClimb).flashing(Colors.Red, Colors.Off))),
			new InstantCommand(pneumatics::extendClimb),
			new InstantCommand(() -> {
				ledA.set(false);
				ledB.set(true);
			}),
			new RaiseArmsSync(leftArm, rightArm),
			trim
		);
	}
}