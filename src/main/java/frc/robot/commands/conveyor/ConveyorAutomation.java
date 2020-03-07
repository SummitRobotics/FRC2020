package frc.robot.commands.conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;

/**
 * Default command for the conveyor to manage its state
 */
public class ConveyorAutomation extends CommandBase {

	private Conveyor conveyor;

	public ConveyorAutomation(Conveyor conveyor) {
		this.conveyor = conveyor;
	
		addRequirements(conveyor);
	}

	@Override
	public void execute() {
		double power = 0;

		switch (conveyor.getState()) {
			case SHOOT: 
				power = Conveyor.SHOOT_POWER;
			case INTAKE: 
				power = intake();
			case OFF: 
				power = 0;
		}

		conveyor.setConveyor(power);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	private double intake() {
		if (conveyor.getBreakBeamEnter() && !conveyor.getBreakBeamExit()) {
			return Conveyor.SUBSUME_POWER;
		}

		return 0;
	}
}