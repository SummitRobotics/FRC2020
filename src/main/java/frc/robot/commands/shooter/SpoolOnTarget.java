package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.LidarLight;
import frc.robot.subsystems.Shooter;

/**
 * THIS SHOULD ONLY BE USED IN A SHOOTING ASSEMBLY, **IT SHOULD NOT BE USED BY ITS SELF**
 */
public class SpoolOnTarget extends CommandBase {

	//TODO - make right
	private static final double TARGET_CLOSE_CUTOFF = 10;

	//WRONG:MAKE GOOD
	private double seesTargetRPM = 1500;
    private double standByRPM = 200;

	private Shooter shooter;
	private LidarLight lidarLight;

	private PIDController pid;

	public SpoolOnTarget(Shooter shooter, LidarLight lidarLight) {
        addRequirements(shooter);
        
		this.shooter = shooter;
		this.lidarLight = lidarLight;

		// TODO - make values good
		pid = new PIDController(0.001, 0.0005, 0);
		//pid.setSetpoint(standByRpm);
        pid.setTolerance(50);
        

        pid.setSetpoint(0);

        SmartDashboard.putData(pid);
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

		if (lidarLight.getHorizontalOffset() < TARGET_CLOSE_CUTOFF) {
            if (lidarLight.getBestDistance() < 155) {
                pid.setSetpoint(2000);
            } else {
                pid.setSetpoint((lidarLight.getBestDistance() * 4.61328) + 1366.23);
            }
            
		} else if (lidarLight.hasTarget()) {
            pid.setSetpoint(seesTargetRPM);
		} else {
			pid.setSetpoint(standByRPM);
        }
        

        double power  = pid.calculate(shooter.getShooterRPM());
        System.out.println("shooter power is: "+ power);

		shooter.setPower(power);
	}

	@Override
	public void end(boolean interupted) {
        pid.reset();
        pid.close();

		shooter.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
    }
}