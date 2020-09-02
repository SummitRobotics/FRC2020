package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShooterTester extends CommandBase {

    private Shooter shooter;

    private boolean shooterStopped;

    public ShooterTester(Shooter shooter) {
        this.shooter = shooter;

        shooterStopped = false;
    }

    @Override
    public void initialize() {
        shooter.stop();
        shooter.setOpenLoopRampRate(10);

        SmartDashboard.putBoolean("Shooter Stopped", false);
    }

    @Override
    public void execute() {
        if (!shooterStopped) {
            shooterStopped = shooter.getRPM() < 1;

            if (shooterStopped) {
                SmartDashboard.putBoolean("Shooter Stopped", true);
                shooter.setPower(1);
            }

            return;
        }

        SmartDashboard.putNumber("Shooter Speed", shooter.getRPM());
        SmartDashboard.putNumber("Current Current", shooter.getCurrentDraw());
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}