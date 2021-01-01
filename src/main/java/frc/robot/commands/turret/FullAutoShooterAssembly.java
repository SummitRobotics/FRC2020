package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.hood.HoodDistanceAngler;
import frc.robot.commands.shooter.SpoolOnTarget;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lidar;
import frc.robot.oi.StatusDisplay;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.ChangeRateLimiter;
import frc.robot.utilities.Colors;
import frc.robot.utilities.Functions;

/**
 * Command for running the full auto mode
 */
public class FullAutoShooterAssembly extends CommandBase {

	private StatusDisplay status;
	private Lidar lidar;
	private Lemonlight limeLight;
	private Conveyor conveyor;
	private Hood hood;
	private Shooter shooter;
	private Turret turret;
	private CommandScheduler scheduler;

	private SpoolOnTarget spool;
	private VisionTarget target;
	private HoodDistanceAngler angler;
	
	private ChangeRateLimiter changeRateLimiter;

	private boolean turretDirection;

	private int badDistanceReadings;

	//WRONG: make good
	private final double acceptableLidarVSLimelightDiscrepancy = 20;

	public FullAutoShooterAssembly(CommandScheduler scheduler, Turret turret, Shooter shooter, Hood hood, Conveyor conveyor, Lemonlight limelight, Lidar lidar, StatusDisplay status) {
		this.status = status;
		this.scheduler = scheduler;
		this.limeLight = limelight;
		this.lidar = lidar;
		this.conveyor = conveyor;
		this.hood = hood;
		this.shooter = shooter;
		this.turret = turret;

		badDistanceReadings = 0;

		changeRateLimiter = new ChangeRateLimiter(turret.max_change_rate);

		turretDirection = true;

		spool = new SpoolOnTarget(shooter, limelight);
		angler = new HoodDistanceAngler(hood);
		target = new VisionTarget(turret, limelight){
			@Override
			protected double noTargetTurretAction(double turretAngle) {
				return turretPasiveAction(turretAngle);
			}
		};
	}

	@Override
	public void initialize() {
		scheduler.schedule(spool, target, angler);
	}

	@Override
	public void execute() {
		//we dont want to feed it bad/random distances if the limelight has no target
		if(limeLight.hasTarget()){
			//looking into the scuedualers code, it should be safe to feed the distance in now beacuse commands are exicuted in the
			//order they are added and this command is added before angler so it should always run first so thet new value will be 
			//loaded into angler before angler.exicute() is called and needs it
			angler.setDistance(getBestDistance());
		}

		if(badDistanceReadings > 10){
			status.addStatus("badReadings", "there have been over 10 bad readings from the lidar", Colors.Yellow, 3);
		}

		//if everything is in position then tell the convayer to shoot
		ShootAction(spool.isUpToShootSpeed() && target.isOnTagret() && angler.isAtTargetAngle());
	}

	private double getBestDistance(){
		double lidarDistance = hood.getCompinsatedLidarDistance(lidar.getAverageDistance());
		double limeLightDisctance = hood.getLimelightDistanceEstmate(limeLight.getVerticalOffset());
		//if the lidar is to far from the limelight distance we use the limelight estmate beacuse it should be more reliable but less acurate
		if(Functions.isWithin(lidarDistance, limeLightDisctance, acceptableLidarVSLimelightDiscrepancy)){
			badDistanceReadings = 0;
			return lidarDistance;
		}
		else{
			badDistanceReadings++;
			return limeLightDisctance;
		}
	}

	@Override
	public void end(boolean interrupted) {
		conveyor.setShootMode(false);
		scheduler.cancel(spool, target, angler);
		
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	//things to be overrriden for semi-auto
	protected void ShootAction(boolean readyToShoot){
		conveyor.setShootMode(readyToShoot);
	}

	protected double turretPasiveAction(double turretAngle){
		//reverses travle direction wehn aproching an end
		if(turretAngle < 5){
			turretDirection = true;
		}
		if(turretAngle > 100){
			turretDirection = false;
		}
		//sets power from direction
		if(turretDirection){
			return changeRateLimiter.getRateLimitedValue(0.2);
		}
		else{
			return changeRateLimiter.getRateLimitedValue(-0.2);
		}
	}
}