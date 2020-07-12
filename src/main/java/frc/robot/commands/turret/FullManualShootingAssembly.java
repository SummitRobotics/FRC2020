package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
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

		if (!(turretRotationPower.inUse() || shooterHoodPower.inUse())) {
			turret.setPower(turretRotationPower.get() / 5); // Scaled by five for sanity
			shooter.setHoodPower(-(shooterHoodPower.get() / 3)); // Scaled by three for proper motor control
		}

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