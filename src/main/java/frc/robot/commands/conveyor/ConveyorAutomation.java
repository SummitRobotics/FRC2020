package frc.robot.commands.conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Conveyor.States;

public class ConveyorAutomation extends CommandBase {

	private Conveyor conveyor;

	/**
	 * Default command for the conveyor to manage its current function based on its state
	 * @param conveyor the conveyor
	 */
	public ConveyorAutomation(Conveyor conveyor) {
		this.conveyor = conveyor;
	
		addRequirements(conveyor);
	}

	@Override
	public void execute() {
		double power = 0;
		
		/**
		 * Sets the conveyors power based on its state. Shoot mode overrides intake mode
		 */
		switch (conveyor.getState()) {
			case SHOOT: 
				power = Conveyor.SHOOT_POWER;
				break;
			case INTAKE: 
				power = intake();
				break;
			case OFF: 
				power = 0;
				break;
		}

		conveyor.setConveyor(power);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	/**
	 * Checks to see if there's a ball in position, and if there is, slurps it in
	 */
	private double intake() {
		if (conveyor.getBreakBeam()) {
			return Conveyor.SUBSUME_POWER;
		}

		return 0;
	}
}