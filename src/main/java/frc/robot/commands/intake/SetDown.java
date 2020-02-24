package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.IntakeArm.States;

public class SetDown extends CommandBase {

    private final static double targetTime = 0.25;
    protected IntakeArm intake;

    protected Timer timer = new Timer();
    protected boolean end;

    protected double startTime;

    private final double intakePower = 0.7;

    public SetDown(IntakeArm intake) {
        this.intake = intake;

        timer.reset();
        timer.start();

        end = false;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        if (intake.getState() == States.DOWN_YES_INTAKE || intake.getState() == States.DOWN_NO_INTAKE) {
            end = true;
        }
        else{
        intake.setState(States.DOWN_YES_INTAKE);
        intake.setPivotPower(0.2);
        intake.setIntakePower(intakePower);

        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPivotPower(0);
    }

    @Override
    public boolean isFinished() {
        return end || timer.get() > targetTime;
    }
}
