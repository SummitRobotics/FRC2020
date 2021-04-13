package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.IntakeArm.States;
import frc.robot.utilities.Functions;

public class IntakeArmDefault extends CommandBase {

    private IntakeArm intake;
    private Timer fakePIDTimer;

    private boolean resetToggle;

    private final static double
    POWER_RAMP_RATE = 1,
    MAX_CORRECTION_POWER = 0.3;

    public IntakeArmDefault(IntakeArm intake) {
        this.intake = intake;

        fakePIDTimer = new Timer();
        resetToggle = true;

        addRequirements(intake);
    }
    
    @Override
    public void execute() {
		//puts up arm if limit switch says it is not up and it should be up
		if (intake.state == States.UP) {
			if (!intake.getUpperLimit()) {
                if (resetToggle) {
                    fakePIDTimer.start();
                    resetToggle = false;
                }
                
                double power = -Functions.clampDouble(
                    fakePIDTimer.get() * MAX_CORRECTION_POWER / POWER_RAMP_RATE, 
                    MAX_CORRECTION_POWER, 
                    -MAX_CORRECTION_POWER
                );

				//fake pid™
				intake.setPivotPower(power);

			} else {
                if (!resetToggle) {
                    resetToggle = true;
                }

				fakePIDTimer.stop();
                fakePIDTimer.reset();
                
				intake.stop();
			}
		}
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
