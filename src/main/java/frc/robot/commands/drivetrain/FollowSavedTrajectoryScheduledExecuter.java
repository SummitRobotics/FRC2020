package frc.robot.commands.drivetrain;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.Functions;
// import frc.robot.utilities.SerialisableMultiGearTrajectory;

public class FollowSavedTrajectoryScheduledExecuter extends CommandBase {

    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(0);

    private Drivetrain drivetrain;
    private String path;

    private int period;

    private Trajectory trajectory;

    private Command splineCommand;
    private ScheduledFuture<?> result;

    public FollowSavedTrajectoryScheduledExecuter(Drivetrain drivetrain, String path) {
        super();

        this.drivetrain = drivetrain;
        this.path = path;

        period = 1;

        this.trajectory = new Trajectory();

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        double[] pid = drivetrain.getPid();

        // try {
        //     SerialisableMultiGearTrajectory both = Functions.RetriveObjectFromFile(path);
        //     trajectory = both.getTrajectory(drivetrain.getShift());
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     System.out.println("Serialized trajectory could not be found/loaded, aborting...");
        //     cancel();
        // }

        // for test
        TrajectoryConfig config = new TrajectoryConfig(2.5, 2.5)
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(Drivetrain.DriveKinimatics)
        // Apply the voltage constraint
        .addConstraint(drivetrain.getVoltageConstraint());

        // scaled by 3 for testing so i dont break my walls
        trajectory = TrajectoryGenerator.generateTrajectory(
            // Start at the origin facing the +X direction
            new Pose2d(0, 0, new Rotation2d(0)),
            // Pass through these two interior waypoints, making an 's' curve path
            List.of(new Translation2d(3, 1).div(3), new Translation2d(6, -1).div(3)),
            // End 3 meters straight ahead of where we started, facing forward
            new Pose2d(new Translation2d(9, 0).div(3), new Rotation2d(0)),
            // Pass config
            config);    

        splineCommand = new RamseteCommand(
            trajectory, 
            drivetrain::getPose,

            // TODO make right
            new RamseteController(2, 0.7), drivetrain.getFeedFoward(), Drivetrain.DriveKinimatics,

            drivetrain::getWheelSpeeds, 
            new PIDController(pid[0], pid[1], pid[2], 0.001),
            new PIDController(pid[0], pid[1], pid[2], 0.001), 
            drivetrain::setMotorVolts, 
            drivetrain
        );

        drivetrain.setPose(trajectory.getInitialPose());

        splineCommand.initialize();

        result = FollowSavedTrajectoryScheduledExecuter.scheduledExecutor
            .scheduleAtFixedRate(() -> {
                System.out.println("Execute start");

                // Dubious thread safety
                synchronized (splineCommand) {
                    // Executes the ramsete command to set drivetrain motor powers
                    splineCommand.execute();
                }

                System.out.println("Execute end");
            }, 0, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isFinished() {
        // Dubious thread safety
        // Allows FSTSE to act as a proxy for splineCommand
        synchronized (splineCommand) {
            return splineCommand.isFinished();
        }
    }

    @Override
    public void end(boolean interrupted) {
        // Cancels the running of the splineCommand
        result.cancel(true); // This should maybe be false ???
        synchronized (splineCommand) {
            splineCommand.end(interrupted);
        }
        drivetrain.stop();
    }
}