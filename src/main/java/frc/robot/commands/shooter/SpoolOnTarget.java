package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.LidarLight;
import frc.robot.oi.inputs.OIButton;
import frc.robot.subsystems.Shooter;

/**
 * THIS SHOULD ONLY BE USED IN A SHOOTING ASSEMBLY, **IT SHOULD NOT BE USED BY ITS SELF**
 */
public class SpoolOnTarget extends CommandBase {

	//TODO - make right
	private static final double TARGET_CLOSE_CUTOFF = 10;

	private static final double TARGET_SIGHTED_RPM = 1500;
    private static final double STAND_BY_RPM = 200;

	private Shooter shooter;
    private LidarLight lidarLight;
    private OIButton superCloseZone;
    
    private boolean lessThanTransfer = false;

	private PIDController pid;

	public SpoolOnTarget(Shooter shooter, LidarLight lidarLight, OIButton superCloseZone) {
        addRequirements(shooter);
        
		this.shooter = shooter;
        this.lidarLight = lidarLight;
        this.superCloseZone = superCloseZone;

        pid = new PIDController(0.0005, 0.0003, 0);
        // pid = new PIDController(0.0008, 0.0005, 0);
		pid.setSetpoint(STAND_BY_RPM);
        pid.setTolerance(100);
		pid.setName("shooter speed pid");
		
		SmartDashboard.putData(pid);
	}

	/**
	 * gets whether the shooter is at an acceptable rpm to shoot a ball
     * 
	 * @return true when acceptable to shoot
	 */
	public boolean isUpToShootSpeed(){
		return pid.atSetpoint();
	}

	@Override
	public void execute() {

        // For closest zone outside of limelight range
        if (superCloseZone.get()) {
            pid.setSetpoint(2000);
            return;
        }

		if (lidarLight.getHorizontalOffset() < TARGET_CLOSE_CUTOFF) {
            double distance = lidarLight.getBestDistance();
            double target = 155;
            double offset = 15;
    
            double out;
            if (lessThanTransfer && distance > target + offset) {
                lessThanTransfer = false;
            } else if (!lessThanTransfer && distance <= target - offset) {
                lessThanTransfer = true;
            }
    
            if (lessThanTransfer && distance < target + offset) {
                out = 2000;
            } else {
                // out = distance * 4.61328 + 1366.23;
                out = distance * 5 + 1336.23; // fucky mods
            }

            out += 70;

            pid.setSetpoint(out);
    
            // if (lidarLight.getBestDistance() < 155) {
            //     pid.setSetpoint(2000);
            // } else {
            //     pid.setSetpoint((lidarLight.getBestDistance() * 4.61328) + 1366.23);
            // }
            
		} else if (lidarLight.hasTarget()) {
            pid.setSetpoint(TARGET_SIGHTED_RPM);
		} else {
			pid.setSetpoint(STAND_BY_RPM);
        }
        

        double power  = pid.calculate(shooter.getShooterRPM());
        // SmartDashboard.putNumber("Shooter Power", power);
        SmartDashboard.putNumber("Shooter Setpoint", pid.getSetpoint());

		shooter.setPower(power);
	}

	@Override
	public void end(boolean interupted) {
        pid.reset();
        pid.close();

		shooter.stop();
	}
}