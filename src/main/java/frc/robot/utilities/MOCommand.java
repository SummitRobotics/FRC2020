package frc.robot.utilities;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.utilities.functionalinterfaces.Binder;

/**
 * abstract class for creatign MOcommands
 */
public abstract class MOCommand extends CommandBase {

	protected MOCommand(Subsystem controlSystem, Subsystem... additionalRequirements) {
		addRequirements(controlSystem);
		addRequirements(additionalRequirements);
	}


    public Command bindCommand(
		Trigger trigger, 
		Binder binding, 
		Command command
	) {
		return bindCommand(trigger, binding, command, true);
	}


	public Command bindCommand(
		Trigger trigger, 
		Binder binding, 
		Command command, 
		boolean interruptable
	) {
		return Functions.bindCommand(this, trigger, binding, command, interruptable);
	}
}