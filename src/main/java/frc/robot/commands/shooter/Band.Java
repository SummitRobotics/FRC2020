package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
 
public class Band extends CommandBase
{
    public Band(Shooter shooter, Orchestra obj) {
        this.shooter = shooter;
        this.Orchestra = obj;

        addRequirements(shooter)
    }

    @Override
    public void execute() {
        shooter.lottaStuff();
    }

    @Override
    public void end(){
        shooter.shooterPause();
        SmartDashboard.putBoolean("Is Playing?", false);
    }

    @Override
    public void isFinished() {
        return false;
    }
}