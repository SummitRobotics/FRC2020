package frc.robot.oi;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.functionalinterfaces.ButtonGetter;

public class LEDButton extends LoggerButton {

	public interface LED {
		public void set(boolean state);
	}

	private Command controller;

	public LEDButton(ButtonGetter getter, LoggerRelations logReference, SyncLogger logger, LED led) {
		super(getter, logReference, logger);

		controller = new StartEndCommand(
			() -> led.set(true),
			() -> led.set(false)
		);
	}

	public LEDButton(ButtonGetter getter, LoggerRelations logReference, LED led) {
		super(getter, logReference);

		controller = new StartEndCommand(
			() -> led.set(true),
			() -> led.set(false)
		);
	}

	public void toggleBind() {
		this.toggleWhenPressed(controller);
	}

	public void pressBind() {
		triggerBind(this);
	}

	public void commandBind(Command command) {
		triggerBind(new Trigger(command::isFinished).negate());
	}

	private void triggerBind(Trigger trigger) {
		trigger.whileActiveOnce(controller);
	}
}