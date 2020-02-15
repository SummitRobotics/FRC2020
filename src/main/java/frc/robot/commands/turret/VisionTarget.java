package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lemonlight.LEDModes;
import frc.robot.subsystems.Turret;

public class VisionTarget extends CommandBase {

	private Turret turret;
	private Lemonlight limelight;

	private PIDController pidController;

	//TODO - Make right
	private final static double
	P = 0,
	I = 0,
	D = 0;

	public VisionTarget(Turret turret, Lemonlight limelight) {
		this.turret = turret;
		this.limelight = limelight;

		pidController = new PIDController(P, I, D);

		addRequirements(turret);
	}

	public void initialize() {
		pidController.setSetpoint(0);
		limelight.setPipeline(0);
		limelight.setLEDMode(LEDModes.FORCE_ON);
		pidController.reset();
	}

	public void execute() {
		double offset = limelight.getHorizontalOffset();

		if (limelight.hasTarget()) {
			double power = pidController.calculate(offset);
			turret.setPower(power);

		} else {
			turret.setPower(0);
			pidController.reset();
		}
	}

	public void end(boolean interrupted) {

	}

	public boolean isFinished() {
		return false;
	}
}