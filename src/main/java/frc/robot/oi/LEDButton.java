package frc.robot.oi;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class LEDButton extends OIButton {

	public interface LED {
		public void set(boolean state);
	}

	private Command controller;

	public LEDButton(BooleanSupplier getter, LED led) {
		super(getter);

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
		triggerBind(new Trigger(command::isScheduled));
	}

	public void booleanSupplierBind(BooleanSupplier supplier) {
		new Trigger(supplier).whileActiveContinuous(controller);
	}

	private void triggerBind(Trigger trigger) {
		trigger.whileActiveOnce(controller);
	}
}