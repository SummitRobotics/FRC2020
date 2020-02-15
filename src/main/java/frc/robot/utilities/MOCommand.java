package frc.robot.utilities;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.utilities.functionalinterfaces.Binder;

public class MOCommand extends CommandBase {

	private Trigger activator;

	public MOCommand(Subsystem... requirements) {
		this.activator = new Trigger(() -> CommandScheduler.getInstance().isScheduled(this));

		addRequirements(requirements);
	}

	public Command bindCommand(Trigger trigger, Binder binding, Command command) {
		bindCommand(trigger, binding, command, true);
		return command;
	}

	public Command bindCommand(Trigger trigger, Binder binding, Command command, boolean interruptable) {
		binding.bind(activator.and(trigger), command, interruptable);
		return command;
	}
}