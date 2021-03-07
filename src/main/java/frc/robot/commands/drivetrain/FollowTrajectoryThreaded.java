// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain;

import java.lang.Thread.State;
import java.util.List;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.Functions;
// import frc.robot.utilities.SerialisableMultiGearTrajectory;

//this is REAL bad
public class FollowTrajectoryThreaded extends CommandBase {

    private Trajectory trajectory;
    private Drivetrain drivetrain;
    private String path;
    private Thread t;
    private int period;
    private ThreadedSplineExecutor sin;

    private RamseteCommand command;

    /**
     * command to folow a trejectory object that has been saved to the roborio with threading to make it more precice
     * @param drivetrain drivetain to control
     * @param path path to the saved SerialisableMultiGearTrejectory object
     */
    public FollowTrajectoryThreaded(Drivetrain drivetrain, Trajectory trajectory) {
        super();

        this.drivetrain = drivetrain;
        this.trajectory = trajectory;

        this.period = 1;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        //TODO make good tune pid for new faster updaes
        double[] pid = drivetrain.getPid();

        command = new RamseteCommand(trajectory, drivetrain::getPose,
                // TODO make right
                new RamseteController(2, 0.7), drivetrain.getFeedFoward(), drivetrain.DriveKinimatics,
                drivetrain::getWheelSpeeds, 
                new PIDController(pid[0], pid[1], pid[2], 0.001),
                new PIDController(pid[0], pid[1], pid[2], 0.001), 
                drivetrain::setMotorVolts, drivetrain);

        drivetrain.setPose(trajectory.getInitialPose());
        
        command.initialize();
        System.out.println("command initialized");

        //we CAN NOT touch the command outside of the thread once it has started
        //nor can we touch the drivetrain but that should be ok beacuse the scedular should handle that
        sin = new ThreadedSplineExecutor(command, period);
        t = new Thread(sin);
        t.setName("spline thread");
        //1-10
        t.setPriority(9);
        t.start();
        System.out.println("thread started");
    }

    @Override
    public boolean isFinished() {
        //checks if thread is running or ended
        return t.getState() == State.TERMINATED;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            // tells the thread to stop
            sin.stopRunning();
        }

        boolean stopped = t.getState() == State.TERMINATED;

        // makes sure thread is stopped before allowing scheduler to continue to prevent unintentional movement
        while(!stopped){
            stopped = t.getState() == State.TERMINATED;
        }
        //stops the drivetrain motors
        drivetrain.stop();
    }

}

//class that is run by the thread to run the 
class ThreadedSplineExecutor extends Thread {
    private Command command;
    private volatile boolean done = false;
    private int period;

    ThreadedSplineExecutor(Command command, int period) {
        super();
        this.command = command;
        this.done = false;
        this.period = period;
    }

    @Override
    //gets called when thread starts
    public void run() {
        super.run();
        while (!command.isFinished() && !done) {

            // Dubious thread safety
            synchronized (command) {
                // Executes the ramsete command to set drivetrain motor powers
                command.execute();
            }

            //System.out.println("command executed!");

            try {
                //sleep period ms to make the timing consistant
                sleep(period, 0);

            } catch (InterruptedException e) {
                // chronic
                e.printStackTrace();
            }
        }
    }

    // Stops the thread if called for by another
    public synchronized void stopRunning(){
        synchronized(command){
            command.cancel();
        }
        done = true;
    }
}
