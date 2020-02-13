package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.IntakeArm;
import frc.robot.utilities.MOCommand;

public class IntakeArmMO extends MOCommand {

    private IntakeArm intakeArm;
    private LoggerAxis controlAxis;

    private static final double INTAKE_DEFAULT_POWER = .75;

    public IntakeArmMO(IntakeArm intakeArm, LoggerAxis controlAxis, LoggerButton controlButton) {
        super();

        this.intakeArm = intakeArm;
        this.controlAxis = controlAxis;

        bindCommand(controlButton, Trigger::whileActiveOnce, new StartEndCommand(
            () -> intakeArm.setIntakePower(INTAKE_DEFAULT_POWER), 
            () -> intakeArm.setIntakePower(0)
        ));

        addRequirements(intakeArm);
    }

    @Override
    public void initialize() {
        intakeArm.stop();
    }

    @Override
    public void execute() {
        intakeArm.setPivotPower(controlAxis.get());
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