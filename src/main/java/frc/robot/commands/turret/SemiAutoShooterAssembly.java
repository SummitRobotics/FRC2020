package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lidar;
import frc.robot.oi.OIAxis;
import frc.robot.oi.OIButton;
import frc.robot.oi.StatusDisplay;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.ChangeRateLimiter;
import frc.robot.utilities.Functions;
import frc.robot.utilities.SimpleButton;

/**
 * Command for running the semi auto mode
 */
//there may be somthing better then a SequentialCommandGroup for this but this was simple and should work
public class SemiAutoShooterAssembly extends SequentialCommandGroup {

	public SemiAutoShooterAssembly
		(
			CommandScheduler scheduler, 
			Turret turret, 
			Shooter shooter, 
			Hood hood, 
			Conveyor conveyor, 
			Lemonlight limelight, 
			Lidar lidar,
			StatusDisplay status,
			OIAxis controlAxis,
			OIButton trigger
		) {

		SimpleButton simpleTriger = new SimpleButton(trigger);
		ChangeRateLimiter rateLimiter = new ChangeRateLimiter(turret.max_change_rate);

		//takes everything from full auto and just adds in the bit of manual control
		addCommands(new FullAutoShooterAssembly(scheduler, turret, shooter, hood, conveyor, limelight, lidar, status){

			@Override
			protected void ShootAction(boolean readyToShoot){
				if(readyToShoot && simpleTriger.get()){
					conveyor.shootOneBall();
				}
			}
		
			@Override
			protected double turretPasiveAction(double turretAngle){
				return Functions.deadzone(0.05, rateLimiter.getRateLimitedValue(controlAxis.get()/3));
			}
		}
		);

	}
}