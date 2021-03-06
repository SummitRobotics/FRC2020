package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.hood.HoodDistanceAngler;
import frc.robot.commands.shooter.SpoolOnTarget;
import frc.robot.devices.LidarLight;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.ChangeRateLimiter;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.lists.LEDPriorities;
import frc.robot.oi.inputs.OIAxis;
import frc.robot.oi.inputs.OIButton;
import frc.robot.oi.shufhellboardwidgets.StatusDisplayWidget;

/**
 * Command for running the full auto mode
 */
public class FullAutoShooterAssembly extends CommandBase {

    private StatusDisplayWidget status;
    private LidarLight lidarlight;
	private Conveyor conveyor;

	private SpoolOnTarget spool;
	private VisionTarget target;
	private HoodDistanceAngler angler;
	
	private ChangeRateLimiter changeRateLimiter;

	private boolean turretDirection = true;

	private int badDistanceReadings = 0;

	private boolean targetLEDCall = false;
    private boolean shootLEDCall = false;

    private OIButton SCZ;
    
    private LEDCall autoTarget = new LEDCall(10000, LEDRange.All).solid(Colors.Blue);
    private LEDCall autoFireReady = new LEDCall(LEDPriorities.shooterReadyToFire, LEDRange.All).flashing(Colors.Yellow, Colors.Off);

	public FullAutoShooterAssembly(
        Turret turret, 
        Shooter shooter, 
        Hood hood, 
        Conveyor conveyor, 
        LidarLight lidarlight, 
        StatusDisplayWidget status,
        OIButton SCZ,
        OIAxis MSC
    ) {
		this.status = status;
        this.lidarlight = lidarlight;
        this.conveyor = conveyor;
        this.SCZ = SCZ;

		changeRateLimiter = new ChangeRateLimiter(Turret.MAX_CHANGE_RATE);

		spool = new SpoolOnTarget(shooter, lidarlight, SCZ);
		angler = new HoodDistanceAngler(hood, lidarlight, SCZ);
		target = new VisionTarget(turret, lidarlight.limelight, true, SCZ, MSC) {
			@Override
			protected double noTargetTurretAction(double turretAngle) {
				return turretPassiveAction(turretAngle);
			}
		};
		addRequirements(turret);
	}

	@Override
	public void initialize() {
		CommandScheduler.getInstance().schedule(spool, target, angler);
		LEDs.getInstance().addCall("fullautoshooting", new LEDCall(10000, LEDRange.All).solid(Colors.Blue));
	}

	@Override
	public void execute() {
		// we don't want to feed it bad/random distances if the limelight has no target
		if (lidarlight.limelight.hasTarget()) {
            autoTarget.activate();
			targetLEDCall = true;

		} else if (targetLEDCall) {
            autoTarget.cancel();
            targetLEDCall = false;
		}

		if (badDistanceReadings > 10) {
			status.addStatus("badReadings", "there have been over 10 bad readings from the lidar", Colors.Yellow, 3);
		}

        boolean ReadyToShoot = (spool.isUpToShootSpeed() && target.isOnTarget() && angler.isAtTargetAngle()) || SCZ.get();
        // System.out.println("spool is " + spool.isUpToShootSpeed() + ", limelight is " + target.isOnTarget() +", hood is "+ angler.isAtTargetAngle());
        SmartDashboard.putBoolean("Has Spool", spool.isUpToShootSpeed());
        SmartDashboard.putBoolean("Has Target", target.isOnTarget());
        SmartDashboard.putBoolean("Has Angle", angler.isAtTargetAngle());

		if (ReadyToShoot && !shootLEDCall) {
            autoFireReady.activate();
			shootLEDCall = true;
        }
        
		else if (!ReadyToShoot && shootLEDCall) {
            autoFireReady.cancel();
			shootLEDCall = false;
		}

		// if everything is in position then tell the conveyor to shoot
		shootAction(ReadyToShoot);
	}

	@Override
	public void end(boolean interrupted) {
		if (targetLEDCall) {
            autoTarget.cancel();
		}

		if (shootLEDCall) {
            autoFireReady.cancel();
		}

		conveyor.setShootMode(false);
		CommandScheduler.getInstance().cancel(spool, target, angler);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	//things to be overrriden for semi-auto
	protected void shootAction(boolean readyToShoot) {
		conveyor.setShootMode(readyToShoot);
	}

	protected double turretPassiveAction(double turretAngle) {
		//reverses travel direction when aproching an end
		if (turretAngle < 40) {
			turretDirection = true;

		} else if(turretAngle > 130) {
			turretDirection = false;
		}

		//sets power from direction
		if (turretDirection) {
			return changeRateLimiter.getRateLimitedValue(0.15);
		} else {
			return changeRateLimiter.getRateLimitedValue(-0.15);
		}
	}
}