package frc.robot.commands.turret;

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
public class SemiAutoShooterAssembly extends FullAutoShooterAssembly {

    private ChangeRateLimiter rateLimiter;
    private Conveyor conveyor;

    private OIButton trigger;
    private OIAxis controlAxis;

    private SimpleButton simpleTrigger;

	public SemiAutoShooterAssembly
		(
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
            super(turret, shooter, hood, conveyor, limelight, lidar, status);

		simpleTrigger = new SimpleButton(trigger);
        rateLimiter = new ChangeRateLimiter(Turret.MAX_CHANGE_RATE);

        this.conveyor = conveyor;
        this.trigger = trigger;
    }
    
    @Override
    protected void shootAction(boolean readyToShoot) {
        if (readyToShoot && simpleTrigger.get() && !trigger.inUse()) {
            conveyor.shootOneBall();
        }
    }

    @Override
    protected double turretPassiveAction(double turretAngle) {
        if(!controlAxis.inUse()){
            return Functions.deadzone(0.05, rateLimiter.getRateLimitedValue(controlAxis.get()/3));
        }
        else{
            return rateLimiter.getRateLimitedValue(0);
        }
    }
}