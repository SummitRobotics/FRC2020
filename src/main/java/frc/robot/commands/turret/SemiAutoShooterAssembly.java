package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.shooter.SpoolOnTarget;
import frc.robot.devices.Lemonlight;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

/**
 * Command for running the semi auto mode
 */
public class SemiAutoShooterAssembly extends ParallelCommandGroup {

	public SemiAutoShooterAssembly
		(
			Turret turret, 
			Shooter shooter, 
			Conveyor conveyor,
			Lemonlight limelight, 
			LoggerAxis controlAxis,
			LoggerButton trigger
		) {

		//Functions.bindCommand(this, new Trigger(), binding, command)

		addCommands(
			new VisionTarget(turret, limelight) {
				@Override
				protected void noTarget() {
					turret.setPower(controlAxis.get());
					conveyor.disableShootMode();
				}

				@Override
				protected void shootControl(){
					if(trigger.get()) {conveyor.enableShootMode();}
					else{conveyor.disableShootMode();}
				}

			},
			new SpoolOnTarget(shooter, limelight)
		);

	}
}