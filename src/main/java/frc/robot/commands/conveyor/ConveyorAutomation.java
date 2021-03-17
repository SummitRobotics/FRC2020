package frc.robot.commands.conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.lists.LEDPriorities;
import frc.robot.subsystems.Conveyor;

public class ConveyorAutomation extends CommandBase {

    private Conveyor conveyor;
    
    private boolean shootModeLED = false;
    private LEDCall conveyorShoot = new LEDCall(LEDPriorities.firing, LEDRange.All).flashing(Colors.Orange, Colors.Off);

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
				if (!shootModeLED) {
					shootModeLED = true;
					conveyorShoot.activate();
                }
                
                break;
                
			case INTAKE: 
				power = intake();
				if (shootModeLED) {
					shootModeLED = false;
					conveyorShoot.cancel();
                }
                
                break;
                
			case OFF: 
				power = 0;
				if (shootModeLED) {
					shootModeLED = false;
					conveyorShoot.cancel();
                }
                
				break;
		}

		conveyor.setConveyor(power);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		if (shootModeLED) {
			shootModeLED = false;
			conveyorShoot.cancel();
        }
        
		conveyor.stop();
	}
	/**
	 * Checks to see if there's a ball in position, and if there is, slurps it in
	 */
	private double intake() {
		if (conveyor.getBreakBeam()) {
            System.out.println("break beam!!!");
			return Conveyor.SUBSUME_POWER;
		}

		return 0;
	}
}