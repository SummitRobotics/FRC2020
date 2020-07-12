package frc.robot.commands.conveyor;

import frc.robot.oi.LoggerAxis;
import frc.robot.subsystems.Conveyor;
import frc.robot.utilities.MOCommand;

public class ConveyorMO extends MOCommand {

	Conveyor conveyor;
	LoggerAxis controlAxis;

	/**
	 * Manual override for the conveyor
	 * @param conveyor the conveyor
	 * @param controlAxis the axis to control the conveyor
	 */
	public ConveyorMO(Conveyor conveyor, LoggerAxis controlAxis) {
		addRequirements(conveyor);
		addUsed(controlAxis);

		this.conveyor = conveyor;
		this.controlAxis = controlAxis;
	}

	@Override
	public void initialize() {
		super.initialize();
		conveyor.stop();
	}

	@Override
	public void execute() {
		conveyor.setConveyor(controlAxis.get());
	}

	@Override
	public void end(final boolean interrupted) {
		super.end(interrupted);
		conveyor.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}