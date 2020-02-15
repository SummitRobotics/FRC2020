package frc.robot.utilities;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.utilities.functionalinterfaces.Binder;

/**
 * Command wrapper for trigger binding
 */
public class MOCommand extends CommandBase {

	private Trigger activator;

	public MOCommand(Subsystem... requirements) {
		this.activator = new Trigger(() -> CommandScheduler.getInstance().isScheduled(this));

		addRequirements(requirements);
	}

	/**
	 * Binds a trigger and command to the function so they only activate when the command is running
	 * 
	 * @param trigger the activation trigger
	 * @param binding the Trigger binding method
	 * @param command the command to be bound
	 * @return the passed in command for convenience
	 */
	public Command bindCommand(Trigger trigger, Binder binding, Command command) {
		bindCommand(trigger, binding, command, true);
		return command;
	}

	/**
	 * Binds a trigger and command to the function so they only activate when the command is running
	 * 
	 * @param trigger the activation trigger
	 * @param binding the Trigger binding method
	 * @param command the command to be bound
	 * @param interruptable whether the command is interruptable
	 * @return the passed in command for convenience
	 */
	public Command bindCommand(Trigger trigger, Binder binding, Command command, boolean interruptable) {
		binding.bind(activator.and(trigger), command, interruptable);
		return command;
	}
}