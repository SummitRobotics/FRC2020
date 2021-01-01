package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lemonlight.LEDModes;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.Functions;

/**
 * Command to vision align the turret
 */
public class VisionTarget extends CommandBase {

	protected Turret turret;
	private Lemonlight limelight;

	private PIDController pidController;

	//TODO - Make right
	private final static double
	P = 0.01,
	I = 0,
	D = 0;

	public VisionTarget(Turret turret, Lemonlight limelight) {
		this.turret = turret;
		this.limelight = limelight;

		pidController = new PIDController(P, I, D);
		pidController.setTolerance(0.1, 1);

		addRequirements(turret);
	}

	public void initialize() {
		pidController.setSetpoint(0);
		limelight.setPipeline(0);
		limelight.setLEDMode(LEDModes.FORCE_ON);
		pidController.reset();
	}

	public void execute() {
		System.out.println("vision");
		if (limelight.hasTarget()) {
			double offset = limelight.getHorizontalOffset();
			double power = pidController.calculate(offset);
			turret.setPower(Functions.clampDouble(power, .33, -.33));
		} else {
			noTarget();
		}
	}

	private void noTarget() {
		pidController.reset();
		turret.setPower(noTargetTurretAction(turret.getAngle()));
	}

	/**
	 * OVERRIDE THIS
	 * this is where the code for what the turret should do when no target is found should go
	 * this should NOT affect the turret directaly
	 * @param turretAngle the curent angle from home of the turret
	 * @return the power fot the turret to move at
	 */
	protected double noTargetTurretAction(double turretAngle){
		return 0;
	}

	public boolean isOnTagret(){
		return pidController.atSetpoint();
	}

	public void end(boolean interrupted) {
		limelight.setLEDMode(LEDModes.FORCE_OFF);
	}

	public boolean isFinished() {
		return false;
	}
}