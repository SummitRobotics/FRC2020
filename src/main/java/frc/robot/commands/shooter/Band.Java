package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import com.ctre.phoenix.music.Orchestra;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; 
 
public class Band extends CommandBase
{
    private Shooter shooter;
    private Orchestra obj;

    public Band(Shooter shooter, Orchestra obj) {
        this.shooter = shooter;
        this.obj = obj;

        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.playSelected();
    }

    @Override
    public void end(boolean interupted){
        shooter.shooterPause();
        SmartDashboard.putBoolean("Is Playing?", false);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}