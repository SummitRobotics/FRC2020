package frc.robot.oi.inputs;

import java.util.function.BooleanSupplier;

import frc.robot.commandegment.Command;
import frc.robot.commandegment.StartEndCommand;
import frc.robot.commandegment.button.Trigger;

public class LEDButton extends OIButton {

	public interface LED {
		public void set(boolean state);
	}

	protected Command controller;

	public LEDButton(BooleanSupplier getter, LED led) {
		super(getter);

		controller = new StartEndCommand(
			() -> led.set(true),
			() -> led.set(false)
		);
	}

	public LEDButton() {
		super();
	}

	private void triggerBind(Trigger trigger) {
		trigger.whileActiveOnce(controller);
	}

	public void toggleBind() {
		this.toggleWhenPressed(controller);
	}

	public void pressBind() {
		triggerBind(this);
	}

	public void commandBind(Command command) {
		triggerBind(new Trigger(command::isScheduled));
	}

	public void booleanSupplierBind(BooleanSupplier supplier) {
		new Trigger(supplier).whileActiveContinuous(controller);
	}
}