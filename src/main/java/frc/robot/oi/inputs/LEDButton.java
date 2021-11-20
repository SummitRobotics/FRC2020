package frc.robot.oi.inputs;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

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

	/**
	 * binds a button light to a command and the button being pressed to a command in 1 function call
	 * @param command the command for the button to be bound to
	 * @param butonAction the method refrence to the action the button should activate on (eg buttonObj::whenPressed)
	 * it might be """ovrcomplicated""" but it saves you having to explicilty define a verable
	 */
	public void commandBind(Command command, Consumer<Command> butonAction){
		butonAction.accept(command);
		commandBind(command);
	}

	public void booleanSupplierBind(BooleanSupplier supplier) {
		new Trigger(supplier).whileActiveContinuous(controller);
	}
}