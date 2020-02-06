package frc.robot.commands.conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Conveyor;

/**
 * Command to give manual control of the conveyor to a HI axis
 */
public class ConveyorMO extends CommandBase {

	Conveyor conveyor;
	LoggerAxis controlAxis;

	public ConveyorMO(Conveyor conveyor, LoggerAxis controlAxis) {
		this.conveyor = conveyor;
		this.controlAxis = controlAxis;

		addRequirements(conveyor);
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