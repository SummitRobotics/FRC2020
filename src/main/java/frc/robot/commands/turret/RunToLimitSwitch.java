package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;

public class RunToLimitSwitch extends CommandBase {

    private Turret turret;

    public RunToLimitSwitch(Turret turret) {
        this.turret = turret;

        addRequirements(turret);
    }

    @Override
    public void initialize() {
        turret.setPower(0.1);
    }

    @Override
    public void execute() {

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(final boolean interrupted) {
        turret.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return turret.getLimitOne();
    }
}