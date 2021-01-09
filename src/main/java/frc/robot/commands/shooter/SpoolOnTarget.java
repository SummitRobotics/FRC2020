package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.subsystems.Shooter;

/**
 * THIS SHOULD ONLY BE USED IN A SHOOTING ASSEMBLY, **IT SHOULD NOT BE USED BY ITS SELF**
 */
public class SpoolOnTarget extends CommandBase {

	//TODO - make right
	private static final double TARGET_CLOSE_CUTOFF = 10;

	//WRONG:MAKE GOOD
	private double onTargetRpm = 2000;

	private double seesTargetRpm = 1000;

	private double standByRpm = 500;

	private Shooter shooter;
	private Lemonlight limelight;

	private PIDController pid;

	public SpoolOnTarget(Shooter shooter, Lemonlight limelight) {
        addRequirements(shooter);
        
		this.shooter = shooter;
		this.limelight = limelight;

		// TODO - make values good
		pid = new PIDController(0.01, 0, 0);
		pid.setSetpoint(standByRpm);
        pid.setTolerance(50);
	}

	/**
	 * gets weather the shooter is at an acceptable rpm to shoot a ball
	 * @return true when acceptable to shoot
	 */
	public boolean isUpToShootSpeed(){
		return pid.atSetpoint();
	}

	@Override
	public void execute() {

		if (limelight.getHorizontalOffset() < TARGET_CLOSE_CUTOFF) {
            pid.setSetpoint(onTargetRpm);
            
		} else if (limelight.hasTarget()) {
            pid.setSetpoint(seesTargetRpm);
            
		} else{
			pid.setSetpoint(standByRpm);
		}

		shooter.setPower(pid.calculate(shooter.getShooterRPM()));
	}

	@Override
	public void end(boolean interupted) {
		shooter.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}