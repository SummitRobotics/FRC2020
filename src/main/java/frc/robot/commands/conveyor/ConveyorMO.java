package frc.robot.commands.conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.oi.LoggerAxis;
import frc.robot.subsystems.Conveyor;
import frc.robot.utilities.MOCommand;

/**
 * Manual override for the conveyor
 */
public class ConveyorMO extends MOCommand {

	Conveyor conveyor;
	LoggerAxis controlAxis;

	public ConveyorMO(Subsystem controlSystem, Conveyor conveyor, LoggerAxis controlAxis) {
		super(controlSystem, conveyor);

		this.conveyor = conveyor;
		this.controlAxis = controlAxis;
	}

	@Override
	public void initialize() {
		conveyor.stop();
	}

	@Override
	public void execute() {
		conveyor.setConveyor(controlAxis.get());
	}

	@Override
	public void end(final boolean interrupted) {
		conveyor.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}