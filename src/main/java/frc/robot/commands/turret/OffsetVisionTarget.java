package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lidar;
import frc.robot.devices.Lemonlight.LEDModes;
import frc.robot.subsystems.Turret;

public class OffsetVisionTarget extends CommandBase {

	private Turret turret;
	private Lemonlight limelight;
	private Lidar lidar;

	private PIDController pidController;

	//TODO - make right
	private static final double
	P = 0,
	I = 0,
	D = 0;

	public OffsetVisionTarget(Turret turret, Lemonlight limelight, Lidar lidar) {
		this.turret = turret;
		this.limelight = limelight;
		this.lidar = lidar;

		pidController = new PIDController(P, I, D);

		addRequirements(turret);
	}

	@Override
	public void initialize() {
		pidController.setSetpoint(getSetpoint());
		pidController.reset();

		limelight.setPipeline(0);
		limelight.setLEDMode(LEDModes.FORCE_ON);
	}

	@Override
	public void execute() {
		if (limelight.hasTarget()) {
			double offset = limelight.getHorizontalOffset();
			pidController.setSetpoint(getSetpoint());

			double power = pidController.calculate(offset);
			turret.setPower(power);

		} else {
			turret.setPower(0);
			pidController.reset();
		}
	}

	@Override
	public void end(boolean interrupted) {

	}

	@Override
	public boolean isFinished() {
		return false;
	}

	private double getSetpoint() {
		double setpoint;

		setpoint = - ((double) Lemonlight.X_OFFSET / lidar.getAverageDistance());
		setpoint = Math.atan(setpoint);

		return setpoint;
	}
}