package frc.robot.commands.conveyor;

import frc.robot.commandegment.CommandBase;
import frc.robot.oi.inputs.OIAxis;
import frc.robot.subsystems.Conveyor;

public class ConveyorMO extends CommandBase {

	Conveyor conveyor;
	OIAxis controlAxis;

	/**
	 * Manual override for the conveyor
	 * @param conveyor the conveyor
	 * @param controlAxis the axis to control the conveyor
	 */
	public ConveyorMO(Conveyor conveyor, OIAxis controlAxis) {
		addRequirements(conveyor);
		setPriority(1);

		this.conveyor = conveyor;
		this.controlAxis = controlAxis;
	}

	@Override
	public void initialize() {
		controlAxis.getWithPriorityOrDeafult(this, getScedualedPriority());
		super.initialize();
		conveyor.stop();
	}

	@Override
	public void execute() {
		conveyor.setConveyor(controlAxis.getWithPriorityOrDeafult(this, 0));
	}

	@Override
	public void end(final boolean interrupted) {
		controlAxis.release(this);
		super.end(interrupted);
		conveyor.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}