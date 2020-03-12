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

	private static int using = 0;

	public static void setDefaultCommand(Command command) {
		new Trigger(() -> (using == 0)).whileActiveContinuous(command);
	}

	@Override
	public void initialize() {
		using++;
	}

	@Override
	public void end(boolean interrupted) {
		using--;
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