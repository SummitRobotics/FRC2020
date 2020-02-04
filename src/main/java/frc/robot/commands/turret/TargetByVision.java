package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.subsystems.Turret;

public class TargetByVision extends CommandBase {

    Turret turret;
    Lemonlight limelight;

    public TargetByVision(Turret turret, Lemonlight limelight) {
        this.turret = turret;
        this.limelight = limelight;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}