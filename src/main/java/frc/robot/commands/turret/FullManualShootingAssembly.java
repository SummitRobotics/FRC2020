package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.lists.LEDPriorities;
import frc.robot.oi.inputs.OIAxis;
import frc.robot.oi.inputs.OIButton;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Hood;
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
	private Hood hood;

	private OIAxis 
	turretRotationPower,
	shooterSpoolPower,
	shooterHoodPower;

	private OIButton trigger;

	private boolean startupSpinPrevention;

	private ChangeRateLimiter limiter;

	public FullManualShootingAssembly (
			Turret turret, 
			Shooter shooter, 
			Hood hood,
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
		this.hood = hood;

		this.turretRotationPower = turretRotationPower;
		this.shooterSpoolPower = shooterSpoolPower;
		this.shooterHoodPower = shooterHoodPower;

		this.trigger = trigger;

		limiter = new ChangeRateLimiter(Turret.MAX_CHANGE_RATE);

		startupSpinPrevention = true;

		addRequirements(turret);
	}

	@Override
	public void initialize() {
		turret.stop();
		LEDs.getInstance().addCall("manualShoot", new LEDCall(LEDPriorities.shooterHasTarget, LEDRange.All).solid(Colors.Yellow));
	}

	@Override
	public void execute() {
		if (!turretRotationPower.inUse() && Functions.absGreater(turretRotationPower.get(), shooterHoodPower.get())) {
			double turretPower = limiter.getRateLimitedValue((turretRotationPower.get()/3)); // Scaled by 5 for sanity
			turret.setPower(Functions.deadzone(.05, turretPower)); 

		} else if (!shooterHoodPower.inUse() && Functions.absGreater(shooterHoodPower.get(), turretRotationPower.get())) {
			turret.setPower(limiter.getRateLimitedValue(0));

			hood.setPower(-(Functions.deadzone(.05, shooterHoodPower.get()) / 3)); // Scaled by 3 for proper motor control

		} else {
			turret.setPower(limiter.getRateLimitedValue(0));
			hood.setPower(0);
		}

		//System.out.println("shoot power: " + shooterSpoolPower.get());

		if (!shooterSpoolPower.inUse()) {
			double shooterPower  = (shooterSpoolPower.get() - 1) / -2;

			if (startupSpinPrevention && (shooterPower > 0.75)) {
				startupSpinPrevention = false;
			}
			
			if (!startupSpinPrevention) {
                shooter.setPower(shooterPower);
                
			} else {
				shooter.setPower(0);
			}
		}
		
		if (!trigger.inUse()) {
	
			conveyor.setShootMode(trigger.get());
		}


		//System.out.println("runnign curent: " + hood.getCurrent() + " curent encoder: " + hood.getEncoder());
	}

	@Override
	public void end(boolean interrupted) {
		conveyor.setShootMode(false);

		turret.stop();
		shooter.stop();
		hood.stop();

		LEDs.getInstance().removeCall("manualShoot");
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}