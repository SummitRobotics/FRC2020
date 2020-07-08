package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.oi.LoggerAxis;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.IntakeArm;
import frc.robot.utilities.Functions;
import frc.robot.utilities.MOCommand;

/**
 * Manual override for the intake arm
 */
public class IntakeArmMOProxy extends MOCommand {

    private IntakeArm intakeArm;
    private LoggerAxis controlAxis;

    private LoggerButton controlButtonA, controlButtonB, controlButtonC;

    private static final double INTAKE_DEFAULT_POWER = .7;

    public IntakeArmMOProxy(IntakeArm intakeArm, LoggerAxis controlAxis, LoggerButton controlButtonA, LoggerButton controlButtonB, LoggerButton controlButtonC) {
        addRequirements(intakeArm);

        this.intakeArm = intakeArm;
        this.controlAxis = controlAxis;

        this.controlButtonA = controlButtonA;
        this.controlButtonB = controlButtonB;
        this.controlButtonC = controlButtonC;

        /*
        bindCommand(controlButton, Trigger::whileActiveOnce, new StartEndCommand(
            () -> {
                intakeArm.setIntakePower(INTAKE_DEFAULT_POWER);
                System.out.println(INTAKE_DEFAULT_POWER);
            }, 
            () -> intakeArm.setIntakePower(0)
        ));
        */

        // Functions.bindCommand(
        //     this, 
        //     controlButton, 
        //     Trigger::whileActiveContinuous, 
        //     new StartEndCommand(
        //         () -> {
        //             intakeArm.setIntakePower(INTAKE_DEFAULT_POWER);
        //             System.out.println(INTAKE_DEFAULT_POWER);
        //         }, 
        //         () -> intakeArm.setIntakePower(0)
        //     )
        // );
    }

    @Override
    public void initialize() {
        super.initialize();
        intakeArm.stop();
    }

    @Override
    public void execute() {
        if (controlButtonB.get()) {
            intakeArm.setIntakePower(controlAxis.get());
        } else {
            if (controlButtonA.get()) {
                intakeArm.setIntakePower(IntakeArm.intakePower);
            } else {
                intakeArm.setIntakePower(0);
            }

            intakeArm.setPivotPower(controlAxis.get());
        }

        intakeArm.setLock(controlButtonC.get());
    }

    @Override
    public void end(final boolean interrupted) {
        super.end(interrupted);
        intakeArm.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}