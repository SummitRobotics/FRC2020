package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;

public class SetDown extends CommandBase {

    IntakeArm intake;

    Timer timer;
    double startTime;

    boolean end;
    double targetTime;

    public SetDown(IntakeArm intake) {
        this.intake = intake;

        timer = new Timer();
        startTime = 0;

        end = false;
        targetTime = 0;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        startTime = timer.get();

        //TODO - tune target times to be right
        switch (intake.state) {
            case UP:
                targetTime = 0;
                break;
            case DOWN:
                end = true;
                break;
            case LOADING:
                targetTime = 0;
                break;
        }
    }

    @Override
    public void execute() {
        if (timer.get() - startTime < targetTime) {
            intake.setPivotPower(0.5);
        } else {
            intake.setPivotPower(0.1);
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPivotPower(0);
    }

    @Override
    public boolean isFinished() {
        return end;
    }
}
