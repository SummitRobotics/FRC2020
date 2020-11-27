package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.OIAxis;
import frc.robot.oi.OIButton;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.ChangeRateLimiter;
import frc.robot.utilities.Functions;

/**
 * Command for running the full manual mode
 */
public class FullManualShootingAssembly extends CommandBase {

	private Turret turret;
	private Shooter shooter;
	private Conveyor conveyor;

	private OIAxis 
	turretRotationPower,
	shooterSpoolPower,
	shooterHoodPower;

	private OIButton trigger;

	private boolean startupSpinPrevention;

	private ChangeRateLimiter limiter;
    private final double max_turret_change_rate = 0.025;

	public FullManualShootingAssembly (
			Turret turret, 
			Shooter shooter, 
			Conveyor conveyor, 
			OIAxis turretRotationPower, 
			OIAxis shooterSpoolPower,
			OIAxis shooterHoodPower,
			OIButton trigger
		) {

		super();

		this.turret = turret;
		this.shooter = shooter;
		this.conveyor = conveyor;

		this.turretRotationPower = turretRotationPower;
		this.shooterSpoolPower = shooterSpoolPower;
		this.shooterHoodPower = shooterHoodPower;

		this.trigger = trigger;

		limiter = new ChangeRateLimiter(max_turret_change_rate);

		startupSpinPrevention = true;

		addRequirements(turret);
	}

	@Override
	public void initialize() {
		turret.stop();
	}

	@Override
	public void execute() {
		if (!turretRotationPower.inUse() && Functions.absoluteGreater(turretRotationPower.get(), shooterHoodPower.get())) {
			double turretPower = limiter.getRateLimitedValue((turretRotationPower.get()/5)); // Scaled by 5 for sanity
			turret.setPower(Functions.deadzone(.05, turretPower)); 

		} else if (!shooterHoodPower.inUse() && Functions.absoluteGreater(shooterHoodPower.get(), turretRotationPower.get())) {
			turret.setPower(limiter.getRateLimitedValue(0));

			shooter.setHoodPower(-(Functions.deadzone(.05, shooterHoodPower.get()) / 3)); // Scaled by 3 for proper motor control

		}
		else{
			turret.setPower(limiter.getRateLimitedValue(0));
			shooter.setHoodPower(0);
		}

		//System.out.println("shoot power: " + shooterSpoolPower.get());

		if (!shooterSpoolPower.inUse()) {
			double shooterPower  = (shooterSpoolPower.get() - 1) / -2;

			if(startupSpinPrevention && (shooterPower > 0.75)){
				startupSpinPrevention = false;
			}
			
			if(!startupSpinPrevention){
				shooter.setPower(shooterPower);
			}

			else{
				shooter.setPower(0);
			}

			
			SmartDashboard.putNumber("shooter speed", shooter.getRPM());
			SmartDashboard.putNumber("shooter temp", shooter.getTemperature());
		}
		
		if (!trigger.inUse()) {
			conveyor.setShootMode(trigger.get());
		}
	}

	@Override
	public void end(boolean interrupted) {
		conveyor.setShootMode(false);

		turret.stop();
		shooter.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}