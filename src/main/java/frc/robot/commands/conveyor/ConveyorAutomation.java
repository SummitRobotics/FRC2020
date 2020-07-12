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

	public void setToIntakeMode(){
		
	}

	@Override
	public void execute() {
		double power = 0;
		
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

	private double intake() {
		if (conveyor.getBreakBeam()) {
			return Conveyor.SUBSUME_POWER;
		}

		return 0;
	}
}