package frc.robot.commands.drivetrain;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;
    
public class FollowTrajectory extends CommandBase {

    private Drivetrain drivetrain;
    private String path;

    private RamseteCommand command;

    public FollowTrajectory(Drivetrain drivetrain, String path) {
        super();

        this.drivetrain = drivetrain;
        this.path = path;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        System.out.println("Trajectory is being initialized");

        double[] pid = drivetrain.getPid();

        Trajectory trajectory = null;
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(path);
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + path, ex.getStackTrace());
        }

        System.out.println(trajectory);

        command = new RamseteCommand(
            trajectory, 
            drivetrain::getPose,
            // TODO make right
            new RamseteController(2, 0.7), 
            drivetrain.getFeedFoward(), 
            drivetrain.DriveKinimatics,
            drivetrain::getWheelSpeeds, 
            new PIDController(pid[0], pid[1], pid[2]),
            new PIDController(pid[0], pid[1], pid[2]), 
            drivetrain::setMotorVolts, drivetrain
        );

        drivetrain.setPose(trajectory.getInitialPose());

        command.initialize();

        System.out.println("Trajectory has been initialized");
    }

    @Override
    public void execute() {
        command.execute();
    }

    @Override
    public boolean isFinished() {
        return command.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            command.cancel();
        }
    }
}
