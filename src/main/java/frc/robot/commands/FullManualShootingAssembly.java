package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.conveyor.Expel;
import frc.robot.commands.shooter.Spool;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.MOCommand;

public class FullManualShootingAssembly extends MOCommand {

	private Turret turret;

	private Command expel;
	private Command spool;

	private LoggerAxis turretRotationPower;

	public FullManualShootingAssembly(
			Turret turret, 
			Shooter shooter, 
			Conveyor conveyor, 
			LoggerAxis turretRotationPower, 
			LoggerButton trigger,
			LoggerButton upper
		) {

		super();

		this.turret = turret;

		expel = bindCommand(trigger, Trigger::whileActiveContinuous, new Expel(conveyor));
		spool = bindCommand(upper, Trigger::whileActiveOnce, new Spool(shooter));

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
		turret.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}