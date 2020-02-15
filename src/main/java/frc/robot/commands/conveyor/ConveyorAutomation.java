package frc.robot.commands.conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;

public class ConveyorAutomation extends CommandBase {

	private Conveyor conveyor;
	private Shooter shooter;
	private Lemonlight limelight; 

	public ConveyorAutomation(Conveyor conveyor, Shooter shooter, Lemonlight limelight) {
		this.conveyor = conveyor;
		this.shooter = shooter;
		this.limelight = limelight;
	
		addRequirements(conveyor);
	}

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {
		double power = 0;

		switch (conveyor.getState()) {
			case SHOOT: 
				power = Conveyor.SHOOT_POWER;
			case SAFE_SHOOT: 
				power = safeShoot();
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

	private double safeShoot() {
		if (limelight.atTarget() && shooter.spooled()) {
			return Conveyor.SHOOT_POWER;
		}

		return 0;
	}

	private double intake() {
		if (conveyor.getBreakBeamEnter() && !conveyor.getBreakBeamExit()) {
			return Conveyor.SUBSUME_POWER;
		}

		return 0;
	}
}