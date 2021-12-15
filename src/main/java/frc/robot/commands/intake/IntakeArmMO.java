package frc.robot.commands.intake;

import frc.robot.commandegment.CommandBase;
import frc.robot.oi.inputs.OIAxis;
import frc.robot.oi.inputs.OIButton;
import frc.robot.subsystems.IntakeArm;
import frc.robot.utilities.lists.commandPriorities;

/**
 * Manual override for the intake arm
 */
public class IntakeArmMO extends CommandBase {

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
        setPriority(commandPriorities.MoPriority);

        this.intakeArm = intakeArm;
        this.controlAxis = controlAxis;

        this.controlButtonA = controlButtonA;
        this.controlButtonB = controlButtonB;
        this.controlButtonC = controlButtonC;
    }

    @Override
    public void initialize() {
        reliceAxies(controlAxis, controlButtonA, controlButtonB, controlButtonC);
        super.initialize();
        intakeArm.stop();

    }

    @Override
    public void execute() {
        if (controlButtonB.get()) {
            intakeArm.setIntakePower(controlAxis.getWithPriority(this));
        } else {
            if (controlButtonA.getWithPriority(this)) {
                intakeArm.setIntakePower(IntakeArm.intakePower);
            } else {
                intakeArm.setIntakePower(0);
            }

            intakeArm.setPivotPower(controlAxis.getWithPriority(this));
        }

        intakeArm.setLock(controlButtonC.getWithPriority(this));
    }

    @Override
    public void end(final boolean interrupted) {
        reliceAxies();
        super.end(interrupted);
        intakeArm.stop();

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}