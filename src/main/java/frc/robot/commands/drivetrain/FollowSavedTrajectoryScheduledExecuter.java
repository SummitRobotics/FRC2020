package frc.robot.commands.drivetrain;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.Functions;
import frc.robot.utilities.SerialisableMultiGearTrajectory;

public class FollowSavedTrajectoryScheduledExecuter extends CommandBase {

    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(0);

    private Drivetrain drivetrain;
    private String path;

    private int period;

    private Trajectory trajectory;

    private Command splineCommand;
    private ScheduledFuture<?> result;

    public FollowSavedTrajectoryScheduledExecuter(Drivetrain drivetain, String path) {
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

        try {
            SerialisableMultiGearTrajectory both = Functions.RetriveObjectFromFile(path);
            trajectory = both.getTrajectory(drivetrain.getShift());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Serialized trajectory could not be found/loaded, aborting...");
            cancel();
        }

        splineCommand = new RamseteCommand(
            trajectory, 
            drivetrain::getPose,

            // TODO make right
            new RamseteController(2, 0.7), drivetrain.getFeedFoward(), drivetrain.DriveKinimatics,

            drivetrain::getWheelSpeeds, 
            new PIDController(pid[0], pid[1], pid[2], period/1000),
            new PIDController(pid[0], pid[1], pid[2], period/1000), 
            drivetrain::setMotorVolts, 
            drivetrain
        );

        drivetrain.setPose(trajectory.getInitialPose());

        splineCommand.initialize();

        result = FollowSavedTrajectoryScheduledExecuter.scheduledExecutor
            .scheduleWithFixedDelay(() -> {
                // Dubious thread safety
                synchronized (splineCommand) {
                    // Executes the ramsete command to set drivetrain motor powers
                    splineCommand.execute();
                }
            }, 0, period, TimeUnit.SECONDS);
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
    }
}