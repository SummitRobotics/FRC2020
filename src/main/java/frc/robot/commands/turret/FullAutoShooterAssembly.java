package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commandegment.CommandBase;
import frc.robot.commandegment.CommandScheduler;
import frc.robot.commands.hood.HoodDistanceAngler;
import frc.robot.commands.shooter.SpoolOnTarget;
import frc.robot.devices.LidarLight;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.ChangeRateLimiter;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.lists.LEDPriorities;
import frc.robot.oi.shufhellboardwidgets.StatusDisplayWidget;

/**
 * Command for running the full auto mode
 */
public class FullAutoShooterAssembly extends CommandBase {

    private StatusDisplayWidget status;
    private LidarLight lidarlight;
	private Conveyor conveyor;
	private Turret turret;
	private Shooter shooter;
	private Hood hood;

	private SpoolOnTarget spool;
	private VisionTarget target;
	private HoodDistanceAngler angler;
	
	private ChangeRateLimiter changeRateLimiter;

	private boolean turretDirection = true;

	private int badDistanceReadings = 0;

	//private boolean targetLEDCall = false;
    private boolean shootLEDCall = false;
    
    //private LEDCall autoTarget = new LEDCall(10000, LEDRange.All).solid(Colors.Blue);
    private LEDCall autoFireReady = new LEDCall(LEDPriorities.shooterReadyToFire, LEDRange.All).flashing(Colors.Yellow, Colors.Off);

	public FullAutoShooterAssembly(
        Turret turret, 
        Shooter shooter, 
        Hood hood, 
        Conveyor conveyor, 
        LidarLight lidarlight, 
        StatusDisplayWidget status
    ) {
		this.turret = turret;
		this.shooter =  shooter;
		this.hood = hood;
		this.status = status;
        this.lidarlight = lidarlight;
        this.conveyor = conveyor;

		changeRateLimiter = new ChangeRateLimiter(Turret.MAX_CHANGE_RATE);

		spool = new SpoolOnTarget(shooter, lidarlight);
		angler = new HoodDistanceAngler(hood, lidarlight);
		target = new VisionTarget(turret, lidarlight.limelight, true) {
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
		//System.out.println("itited full shooter");
		//LEDs.getInstance().addCall("fullautoshooting", new LEDCall(10000, LEDRange.All).solid(Colors.Blue));
	}

	@Override
	public void execute() {
		//System.out.println("full shooter run");
		// we don't want to feed it bad/random distances if the limelight has no target
		// if (lidarlight.limelight.hasTarget()) {
        //     autoTarget.activate();
		// 	targetLEDCall = true;

		// } else if (targetLEDCall) {
        //     autoTarget.cancel();
        //     targetLEDCall = false;
		// }

		if (badDistanceReadings > 10) {
			status.addStatus("badReadings", "there have been over 10 bad readings from the lidar", Colors.Yellow, 3);
		}

        boolean ReadyToShoot = (spool.isUpToShootSpeed() && target.isOnTarget() && angler.isAtTargetAngle());
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
		// if (targetLEDCall) {
        //     autoTarget.cancel();
		// }

		if (shootLEDCall) {
            autoFireReady.cancel();
		}

		conveyor.setShootMode(false);
		CommandScheduler.getInstance().cancel(spool, target, angler);

		//System.out.println("full shooter end " + interrupted);
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

		if (turretAngle < Turret.encoderToAngle(Turret.shootingBackLimit)) {
			turretDirection = true;

		} else if(turretAngle > Turret.encoderToAngle(Turret.fowardLimit)) {
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