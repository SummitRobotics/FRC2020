package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class SemiAutoShooterAssembly extends ParallelCommandGroup {

	public SemiAutoShooterAssembly(Turret turret, Shooter shooter, Conveyor conveyor,  LoggerButton trigger) {

	}
}