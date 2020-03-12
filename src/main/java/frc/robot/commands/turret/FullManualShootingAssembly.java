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

	private Command expel;

	private LoggerAxis 
	turretRotationPower,
	shooterSpoolPower,
	shooterHoodPower;

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

		expel = Functions.bindCommand(this, trigger, Trigger::whileActiveOnce, conveyor.toggleShootMode);

		this.turretRotationPower = turretRotationPower;
		this.shooterSpoolPower = shooterSpoolPower;
		this.shooterHoodPower = shooterHoodPower;

		addRequirements(turret);
	}

	@Override
	public void initialize() {
		turret.stop();
	}

	@Override
	public void execute() {
		turret.setPower(turretRotationPower.get() / 5); // Scaled by five for sanity
		shooter.setHoodPower(-(shooterHoodPower.get() / 3)); // Scaled by three for proper motor control

		double spoolPower = (shooterSpoolPower.get() - 1) / 2;
		shooter.setPower(spoolPower);
	}

	@Override
	public void end(boolean interupted) {
		expel.cancel();

		turret.stop();
		shooter.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}