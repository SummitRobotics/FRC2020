package frc.robot.commands.conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.lists.Colors;
import frc.robot.lists.LEDPriorities;
import frc.robot.subsystems.Conveyor;

public class ConveyorAutomation extends CommandBase {

	private Conveyor conveyor;
	private boolean shootModeLED;

	/**
	 * Default command for the conveyor to manage its current function based on its state
	 * @param conveyor the conveyor
	 */
	public ConveyorAutomation(Conveyor conveyor) {
		this.conveyor = conveyor;

		shootModeLED = false;
	
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
				if(!shootModeLED){
					shootModeLED = true;
					LEDs.getInstance().addCall("convayerShoot", new LEDCall(LEDPriorities.firing, LEDRange.All).flashing(Colors.Orange, Colors.Off));
				}
				break;
			case INTAKE: 
				power = intake();
				if(shootModeLED){
					LEDs.getInstance().removeCall("convayerShoot");
				}
				break;
			case OFF: 
				power = 0;
				if(shootModeLED){
					LEDs.getInstance().removeCall("convayerShoot");
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
		if(shootModeLED){
			LEDs.getInstance().removeCall("convayerShoot");
		}
		conveyor.stop();
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