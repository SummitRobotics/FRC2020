package frc.robot.commands.climb;

import frc.robot.commandegment.Command;
import frc.robot.commandegment.InstantCommand;
import frc.robot.commandegment.ParallelCommandGroup;
import frc.robot.commandegment.SequentialCommandGroup;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberPneumatics;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.lists.LEDPriorities;
import frc.robot.oi.inputs.OIAxis;
import frc.robot.oi.inputs.OIButton;
import frc.robot.oi.inputs.LEDButton.LED;

public class ClimbSequence extends SequentialCommandGroup {

	private OIButton next;

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
		LED ledB
	) {
        
        LEDCall armsUp = new LEDCall(LEDPriorities.armsUp, LEDRange.All).flashing(Colors.Red, Colors.Off);

		setPriority(1);

		this.next = nextPhase;
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

				System.out.println("climber interupted is: " + interrupted);

				armsUp.cancel();

				if (!interrupted) {
					pneumatics.retractClimb();
				}
			}
		};

		addCommands(
			new InstantCommand(armsUp::activate),
			new InstantCommand(pneumatics::extendClimb),
			new InstantCommand(() -> {
				ledA.set(true);
				ledB.set(true);
			}),
			new RaiseArmsSync(leftArm, rightArm),
			trim
		);
	}

	@Override
	public void initialize() {
		registerAxies(next);
		super.initialize();
	}

	@Override
	public void end(boolean interrupted) {
		super.end(interrupted);
		reliceAxies();
	}
}