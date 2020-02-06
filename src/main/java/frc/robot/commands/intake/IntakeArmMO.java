package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.IntakeArm;

public class IntakeArmMO extends CommandBase {

    private IntakeArm intakeArm;
    private LoggerAxis controlAxis;
    private LoggerButton controlButton;

    private boolean toggle;

    private static final double INTAKE_DEFAULT_POWER = .75;

    public IntakeArmMO(IntakeArm intakeArm, LoggerAxis controlAxis, LoggerButton controlButton) {
        this.intakeArm = intakeArm;
        this.controlAxis = controlAxis;
        this.controlButton = controlButton;

        toggle = false;

        addRequirements(intakeArm);
    }

    @Override
    public void initialize() {
        intakeArm.stop();
    }

    @Override
    public void execute() {
        intakeArm.setPivotPower(controlAxis.get());

        if (!toggle && controlButton.get()) {
            intakeArm.setIntakePower(INTAKE_DEFAULT_POWER);
            toggle = !toggle;

        } else if (toggle && !controlButton.get() ) {
            intakeArm.setIntakePower(0);
            toggle = !toggle;
        }
    }

    @Override
    public void end(final boolean interrupted) {
        intakeArm.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}