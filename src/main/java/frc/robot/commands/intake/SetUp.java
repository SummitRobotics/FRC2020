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
        }
        else{
        intake.setState(States.UP);
        intake.setPivotPower(-0.3);
        intake.setIntakePower(0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPivotPower(0);
    }

    @Override
    public boolean isFinished() {
        return intake.getUpperLimit() || end;
    }
}
