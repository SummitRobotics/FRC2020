package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.shooter.Spool;
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

	private Command 
	expel,
	spool;

	private LoggerAxis turretRotationPower;

	public FullManualShootingAssembly 
		(
			Turret turret, 
			Shooter shooter, 
			Conveyor conveyor, 
			LoggerAxis turretRotationPower, 
			LoggerButton trigger,
			LoggerButton upper
		) {

		super();

		this.turret = turret;

		expel = Functions.bindCommand(this, trigger, Trigger::whileActiveOnce, conveyor.toggleShootMode);
		spool = Functions.bindCommand(this, upper, Trigger::whileActiveOnce, new Spool(shooter));

		this.turretRotationPower = turretRotationPower;

		addRequirements(turret);
	}

	@Override
	public void initialize() {
		turret.stop();
	}

	@Override
	public void execute() {
		turret.setPower(turretRotationPower.get());
	}

	@Override
	public void end(boolean interupted) {
		expel.cancel();
		spool.cancel();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}