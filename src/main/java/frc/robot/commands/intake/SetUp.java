package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm.States;
import frc.robot.subsystems.IntakeArm;

public class SetUp extends CommandBase {

    IntakeArm intake;
    boolean end;

    public SetUp(IntakeArm intake) {
        this.intake = intake;
        end = false;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        if (intake.getState() == States.UP) {
            end = true;
            
        } else {

            if (intake.getState() == States.DOWN_YES_INTAKE) {
                intake.setIntakePower(IntakeArm.intakePower);
            }

            intake.setState(States.UP);

            intake.setPivotPower(-0.3);
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPivotPower(0);
        intake.setIntakePower(0);

        end = false;
    }

    @Override
    public boolean isFinished() {
        System.out.println(intake.getUpperLimit());
        return intake.getUpperLimit() || end;
    }
}
