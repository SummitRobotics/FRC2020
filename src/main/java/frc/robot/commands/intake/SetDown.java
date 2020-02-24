package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;

public class SetDown extends CommandBase {

    private final static double targetTime = 0.25;
    protected IntakeArm intake;

    protected Timer timer;
    protected boolean end;

    protected double startTime;

    public SetDown(IntakeArm intake) {
        this.intake = intake;

        timer = new Timer();
        timer.start();
        startTime = 0;

        end = false;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        if (!intake.isUp) {
            end = true;
            return;
        }

        intake.isUp = false;
        startTime = timer.get();

        intake.brake();
        intake.setPivotPower(0.2);
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPivotPower(0);
    }

    @Override
    public boolean isFinished() {
        System.out.println(timer.get() - startTime);
        System.out.println(end || timer.get() - startTime > targetTime);
        return end || timer.get() - startTime > targetTime;
    }
}
