package frc.robot.commands.turret;

import frc.robot.devices.LidarLight;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.oi.inputs.OIAxis;
import frc.robot.oi.inputs.OIButton;
import frc.robot.oi.shufhellboardwidgets.StatusDisplayWidget;
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
        LidarLight lidarlight, 
        StatusDisplayWidget status,
        OIAxis controlAxis,
        OIButton trigger
    ) {
        super(turret, shooter, hood, conveyor, lidarlight, status);

		simpleTrigger = new SimpleButton(trigger);
        rateLimiter = new ChangeRateLimiter(Turret.MAX_CHANGE_RATE);

        this.controlAxis = controlAxis;
        this.trigger = trigger;

        this.conveyor = conveyor;
        this.trigger = trigger;
    }

    @Override
    public void initialize() {
        super.initialize();
        LEDs.getInstance().removeCall("fullautoshooting");
    }
    
    @Override
    protected void shootAction(boolean readyToShoot) {
        if (readyToShoot && trigger.get() && !trigger.inUse()) {
            // conveyor.shootOneBall();
            conveyor.setShootMode(true);
        } else {
            conveyor.setShootMode(false);
        }
    }

    @Override
    protected double turretPassiveAction(double turretAngle) {
        if (!controlAxis.inUse()) {
            return Functions.deadzone(0.05, rateLimiter.getRateLimitedValue(controlAxis.get()/3));
        } else {
            return rateLimiter.getRateLimitedValue(0);
        }
    }
}