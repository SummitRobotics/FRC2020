package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.hood.HoodDistanceAngler;
import frc.robot.commands.shooter.SpoolOnTarget;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lidar;
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
import frc.robot.oi.shufhellboardwidgets.StatusDisplayWidget;
import frc.robot.utilities.Functions;

/**
 * Command for running the full auto mode
 */
public class FullAutoShooterAssembly extends CommandBase {

    private StatusDisplayWidget status;
    private LidarLight lidarlight;
	private Conveyor conveyor;
	private Hood hood;

	private SpoolOnTarget spool;
	private VisionTarget target;
	private HoodDistanceAngler angler;
	
	private ChangeRateLimiter changeRateLimiter;

	private boolean turretDirection;

	private int badDistanceReadings;

	private boolean targetLEDCall;
	private boolean shootLEDCall;

	public FullAutoShooterAssembly(Turret turret, Shooter shooter, Hood hood, Conveyor conveyor, LidarLight lidarlight, StatusDisplayWidget status) {
		this.status = status;
        this.lidarlight = lidarlight;
        this.conveyor = conveyor;
		this.hood = hood;

		badDistanceReadings = 0;

		changeRateLimiter = new ChangeRateLimiter(Turret.MAX_CHANGE_RATE);

		turretDirection = true;

		targetLEDCall = false;
		shootLEDCall = false;

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
		LEDs.getInstance().addCall("a", new LEDCall(10000, LEDRange.All).solid(Colors.Blue));
	}

	@Override
	public void execute() {
		// we don't want to feed it bad/random distances if the limelight has no target
		if (lidarlight.limelight.hasTarget()) {
			LEDs.getInstance().addCall("autoTarget", new LEDCall(LEDPriorities.shooterHasTarget, LEDRange.All).solid(Colors.Yellow));
			targetLEDCall = true;

		} else if (targetLEDCall) {
            LEDs.getInstance().removeCall("autoTarget");
            targetLEDCall = false;
		}

		if (badDistanceReadings > 10) {
			status.addStatus("badReadings", "there have been over 10 bad readings from the lidar", Colors.Yellow, 3);
		}

		boolean ReadyToShoot = spool.isUpToShootSpeed() && target.isOnTarget() && angler.isAtTargetAngle();

		if (ReadyToShoot && !shootLEDCall) {
			LEDs.getInstance().addCall("autoFireReady", new LEDCall(LEDPriorities.shooterReadyToFire, LEDRange.All).flashing(Colors.Yellow, Colors.Off));
			shootLEDCall = true;
        }
        
		else if (!ReadyToShoot && shootLEDCall) {
			LEDs.getInstance().removeCall("autoFireReady");
			shootLEDCall = false;
		}

		// if everything is in position then tell the conveyor to shoot
		shootAction(ReadyToShoot);
	}

	@Override
	public void end(boolean interrupted) {
		if (targetLEDCall) {
			LEDs.getInstance().removeCall("autoTarget");
		}

		if (shootLEDCall) {
			LEDs.getInstance().removeCall("autoFireReady");
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