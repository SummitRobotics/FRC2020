package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.IntakeArm.Positions;

public class SetUp extends CommandBase {

    IntakeArm intake;

    public SetUp(IntakeArm intake) {
        this.intake = intake;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.state = Positions.UP;
    }

    @Override
    public void execute() {
        //TODO - tune power
        intake.setPivotPower(-0.5);
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPivotPower(0);
    }

    @Override
    public boolean isFinished() {
        return intake.getUpperLimit();
    }
}
