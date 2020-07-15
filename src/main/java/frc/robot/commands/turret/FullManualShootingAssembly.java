package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.Functions;

/**
 * Command for running the full manual mode
 */
public class FullManualShootingAssembly extends CommandBase {

	private Turret turret;
	private Shooter shooter;
	private Conveyor conveyor;

	private LoggerAxis 
	turretRotationPower,
	shooterSpoolPower,
	shooterHoodPower;

	private LoggerButton trigger;

	public FullManualShootingAssembly 
		(
			Turret turret, 
			Shooter shooter, 
			Conveyor conveyor, 
			LoggerAxis turretRotationPower, 
			LoggerAxis shooterSpoolPower,
			LoggerAxis shooterHoodPower,
			LoggerButton trigger
		) {

		super();

		this.turret = turret;
		this.shooter = shooter;
		this.conveyor = conveyor;

		this.turretRotationPower = turretRotationPower;
		this.shooterSpoolPower = shooterSpoolPower;
		this.shooterHoodPower = shooterHoodPower;

		this.trigger = trigger;

		addRequirements(turret);
	}

	@Override
	public void initialize() {
		turret.stop();
	}

	@Override
	public void execute() {

		if (!turretRotationPower.inUse() && Functions.absoluteGreater(turretRotationPower.get(), shooterHoodPower.get())) {

			turret.setPower(Functions.deadzone(.05, turretRotationPower.get()) / 5); // Scaled by 5 for sanity

		} else if (!shooterHoodPower.inUse() && Functions.absoluteGreater(shooterHoodPower.get(), turretRotationPower.get())) {

			shooter.setHoodPower(-(Functions.deadzone(.05, shooterHoodPower.get()) / 3)); // Scaled by 3 for proper motor control

		}

		//System.out.println("shoot power: " + shooterSpoolPower.get());

		if (!shooterSpoolPower.inUse()) {
			shooter.setPower((shooterSpoolPower.get() - 1) / 2);
		}
		
		if (!trigger.inUse()) {
			conveyor.setShootMode(trigger.get());
		}
	}

	@Override
	public void end(boolean interupted) {
		conveyor.setShootMode(false);

		turret.stop();
		shooter.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}