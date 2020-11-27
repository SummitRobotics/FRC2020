package frc.robot.commands.intake;


import frc.robot.oi.OIAxis;
import frc.robot.oi.OIButton;
import frc.robot.subsystems.IntakeArm;
import frc.robot.utilities.MOCommand;

/**
 * Manual override for the intake arm
 */
public class IntakeArmMO extends MOCommand {

    private IntakeArm intakeArm;
    private OIAxis controlAxis;

    private OIButton controlButtonA, controlButtonB, controlButtonC;

    public IntakeArmMO(
        IntakeArm intakeArm, 
        OIAxis controlAxis, 
        OIButton controlButtonA, 
        OIButton controlButtonB, 
        OIButton controlButtonC
    ) {
        addRequirements(intakeArm);
        addUsed(controlAxis, controlButtonA, controlButtonB, controlButtonC);

        this.intakeArm = intakeArm;
        this.controlAxis = controlAxis;

        this.controlButtonA = controlButtonA;
        this.controlButtonB = controlButtonB;
        this.controlButtonC = controlButtonC;
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