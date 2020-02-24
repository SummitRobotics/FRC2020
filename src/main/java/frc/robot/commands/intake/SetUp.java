package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
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
        if (intake.isUp) {
            end = true;
            return;
        }

        intake.isUp = true;
        intake.brake();
    }

    @Override
    public void execute() {
        //TODO - tune power
        intake.setPivotPower(-0.3);
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPivotPower(-0.1);
    }

    @Override
    public boolean isFinished() {
        return intake.getUpperLimit();
    }
}
