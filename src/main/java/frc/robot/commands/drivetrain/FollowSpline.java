package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;

public class FollowSpline extends RamseteCommand {
    
    private final Drivetrain drivetrain;

    public FollowSpline(Drivetrain drivetrain, Trajectory trajectory) {
        super(
            trajectory,
            drivetrain::getPose,
            new RamseteController(),
            Drivetrain.DRIVE_KINEMATICS,
            drivetrain::setMotorVelocityTargets,
            drivetrain
        );

        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        
    }
}