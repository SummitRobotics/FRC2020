package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.LoggerAxis;
import frc.robot.subsystems.Turret;

public class FullManual extends CommandBase {

	private Turret turret;
	private LoggerAxis controlAxis;

	public FullManual(Turret turret, LoggerAxis controlAxis) {
		this.turret = turret;
		this.controlAxis = controlAxis;
	}
}