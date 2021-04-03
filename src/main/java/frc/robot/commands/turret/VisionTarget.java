package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lemonlight.CamModes;
import frc.robot.devices.Lemonlight.LEDModes;
import frc.robot.oi.inputs.OIAxis;
import frc.robot.oi.inputs.OIButton;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.Functions;

/**
 * THIS SHOULD ONLY BE USED IN A SHOOTING ASSEMBLY, **IT SHOULD NOT BE USED BY ITSELF**
 */
public abstract class VisionTarget extends CommandBase {

	protected Turret turret;
    private Lemonlight limelight;
    private OIButton superCloseZone;
    private OIAxis manualSuperClose;

    private PIDController pidController;
    
    private TurretToPosition ttp;

	private double error = 1;

	//WRONG - Make right 
	private final static double
	P = 0.012,
	I = 0,
	D = 0;

	public VisionTarget(Turret turret, Lemonlight limelight, boolean partOfFullAuto, OIButton superCloseZone, OIAxis manualSuperClose) {
		this.turret = turret;
        this.limelight = limelight;
        this.superCloseZone = superCloseZone;
        this.manualSuperClose = manualSuperClose;

		pidController = new PIDController(P, I, D);
		pidController.setTolerance(error, 1);
        pidController.setName("target pid");
        
        ttp = new TurretToPosition(turret, 90);

        // SmartDashboard.putData(pidController);
        
        
		if (!partOfFullAuto){
			addRequirements(turret);
		}
	}

	public void initialize() {
		pidController.setSetpoint(0);
		limelight.setPipeline(0);
        limelight.setLEDMode(LEDModes.PIPELINE);
        limelight.setCamMode(CamModes.VISION_PROCESSOR);
        pidController.reset();
        
        ttp.initialize();
	}

	public void execute() {
        if (superCloseZone.get()) {
            ttp.execute();
            return;
        }

		if (limelight.hasTarget()) {
			double offset = -(limelight.getSmoothedHorizontalOffset());
			double power = pidController.calculate(offset);
			if (turret.isAtLimit()) {
				//makes sure integral does not get out of control when it cant move any more in the target direction
				pidController.reset();
			}
			turret.setPower(power);
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
		//return pidController.atSetpoint();
		//System.out.println("vision target error is: " + pidController.getPositionError());
		return Math.abs(pidController.getPositionError()) < error;
	}

	public void end(boolean interrupted) {
        pidController.reset();
		pidController.close();

        limelight.setLEDMode(LEDModes.FORCE_OFF);
        
        ttp.end(interrupted);
	}

	public boolean isFinished() {
		return false;
	}
}