package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lemonlight.LEDModes;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.Functions;

/**
 * THIS SHOULD ONLY BE USED IN A SHOOTING ASSEMBLY, **IT SHOULD NOT BE USED BY ITSELF**
 */
public abstract class VisionTarget extends CommandBase {

	protected Turret turret;
	private Lemonlight limelight;

	private PIDController pidController;

	//WRONG - Make right 
	private final static double
	P = 0.01,
	I = 0,
	D = 0;

	public VisionTarget(Turret turret, Lemonlight limelight, boolean partOfFullAuto) {
		this.turret = turret;
		this.limelight = limelight;

		pidController = new PIDController(P, I, D);
		pidController.setTolerance(0.1, 1);

		if (!partOfFullAuto){
			addRequirements(turret);
		}
		
	}

	public void initialize() {
		pidController.setSetpoint(0);
		limelight.setPipeline(0);
		limelight.setLEDMode(LEDModes.FORCE_ON);
		pidController.reset();
	}

	public void execute() {
		if (limelight.hasTarget()) {
			double offset = limelight.getHorizontalOffset();
			double power = pidController.calculate(offset);
			if (turret.isAtLimit()) {
				//makes sure intagral does not get out of control when it cant move any more in the target direction
				pidController.reset();
			}
			turret.setPower(Functions.clampDouble(power, .33, -.33));
		} else {
			noTarget();
		}
	}

	private void noTarget() {
		pidController.reset();
		double power = noTargetTurretAction(turret.getAngle());
		turret.setPower(power);
	}

	/**
	 * this is where the code for what the turret should do when no target is found should go
	 * this should NOT affect the turret directly
	 * @param turretAngle the curent angle of the turret
	 * @return the power for the turret to move at
	 */
	protected abstract double noTargetTurretAction(double turretAngle);

	public boolean isOnTarget(){
		return pidController.atSetpoint();
	}

	public void end(boolean interrupted) {
        pidController.reset();
		pidController.close();

		limelight.setLEDMode(LEDModes.FORCE_OFF);
	}

	public boolean isFinished() {
		return false;
	}
}