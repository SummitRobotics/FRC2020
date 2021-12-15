package frc.robot.commands.conveyor;

import frc.robot.commandegment.CommandBase;
import frc.robot.oi.inputs.OIAxis;
import frc.robot.subsystems.Conveyor;
import frc.robot.utilities.lists.commandPriorities;

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
		setPriority(commandPriorities.MoPriority);

		this.conveyor = conveyor;
		this.controlAxis = controlAxis;
	}

	@Override
	public void initialize() {
		registerAxies(controlAxis);
		controlAxis.getWithPriority(this);
		super.initialize();
		conveyor.stop();
	}

	@Override
	public void execute() {
		conveyor.setConveyor(controlAxis.getWithPriority(this));
	}

	@Override
	public void end(final boolean interrupted) {
		reliceAxies();
		super.end(interrupted);
		conveyor.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}