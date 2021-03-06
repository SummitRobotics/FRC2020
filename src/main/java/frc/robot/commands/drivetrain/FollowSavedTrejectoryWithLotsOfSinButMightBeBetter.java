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
import frc.robot.utilities.SerialisableMultiGearTrejectory;

//this is REAL bad
public class FollowSavedTrejectoryWithLotsOfSinButMightBeBetter extends CommandBase {

    private Trajectory trajectory;
    private Drivetrain drivetrain;
    private String path;
    private Thread t;
    private int period;
    private sin sin;

    private RamseteCommand command;

    /**
     * command to folow a trejectory object that has been saved to the roborio with threding to make it more precice
     * @param drivetrain drivetain to control
     * @param path path to the saved SerialisableMultiGearTrejectory object
     */
    public FollowSavedTrejectoryWithLotsOfSinButMightBeBetter(Drivetrain drivetrain, String path) {
        super();

        this.path = path;
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
        this.period = 1;
    }

    @Override
    public void initialize() {
        //TODO make good tune pid for new faster updaes
        double[] pid = drivetrain.getPid();

        //for test
        // TrajectoryConfig config = new TrajectoryConfig(2.5, 2.5)
        //     // Add kinematics to ensure max speed is actually obeyed
        //     .setKinematics(drivetrain.DriveKinimatics)
        //     // Apply the voltage constraint
        //     .addConstraint(drivetrain.getVoltageConstraint());

        // // scaled by 3 for testing so i dont break my walls
        // Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
        //     // Start at the origin facing the +X direction
        //     new Pose2d(0, 0, new Rotation2d(0)),
        //     // Pass through these two interior waypoints, making an 's' curve path
        //     List.of(new Translation2d(3, 1).div(3), new Translation2d(6, -1).div(3)),
        //     // End 3 meters straight ahead of where we started, facing forward
        //     new Pose2d(new Translation2d(9, 0).div(3), new Rotation2d(0)),
        //     // Pass config
        //     config);

            //real and good for real use but requires running on robot
        try {
            SerialisableMultiGearTrejectory both = Functions.RetriveObjectFromFile(path);
            trajectory = both.getTrajectory(drivetrain.getShift());
        } catch (Exception e) {
            e.printStackTrace();
            throw(new RuntimeException("reading failed"));
        }

        command = new RamseteCommand(trajectory, drivetrain::getPose,
                // TODO make right
                new RamseteController(2, 0.7), drivetrain.getFeedFoward(), drivetrain.DriveKinimatics,
                drivetrain::getWheelSpeeds, 
                new PIDController(pid[0], pid[1], pid[2], period/1000),
                new PIDController(pid[0], pid[1], pid[2], period/1000), 
                drivetrain::setMotorVolts, drivetrain);

        drivetrain.setPose(trajectory.getInitialPose());

        
        command.initialize();
        System.out.println("command initlised");

        //we CAN NOT touch the command outside of the thred once it has started
        //nor can we touch the drivetrain but that should be ok beacuse the scedular should handle that
        sin = new sin(command, period);
        t = new Thread(sin);
        t.setName("spline thred");
        //1-10
        t.setPriority(9);
        t.start();
        System.out.println("thred started");
    }

    @Override
    public void execute() {
        // does nothing now beacuse thred
    }

    @Override
    public boolean isFinished() {
        //checks if thred is running or ended
        return t.getState() == State.TERMINATED;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            //tells the thred to stop
            sin.stopRunning();
        }
        boolean stopped = t.getState() == State.TERMINATED;
        //makes sure thred is stopped before allowing scedular to continue to prevent unintentional movement
        while(!stopped){
            stopped = t.getState() == State.TERMINATED;
        }
        //stops the drivetrain motors
        drivetrain.stop();
    }

}

//class that is run by the thred to run the 
class sin extends Thread {
    private Command command;
    private volatile boolean done = false;
    private int period;

    sin(Command command, int period) {
        super();
        this.command = command;
        this.done = false;
        this.period = period;
    }

    @Override
    //gets called when thred starts
    public void run() {
        super.run();
        while (!command.isFinished() && !done) {
            //thred saftey??? (i tryed)
            synchronized(command){
                //calls the exicute of the ramset command to set new motor powers
                command.execute();
            }
            //System.out.println("command exicuted!");
            try {
                //sleep period ms to make the itmeing consistant
                sleep(period, 0);
            } catch (InterruptedException e) {
                //chronic
                e.printStackTrace();
            }
        }
    }

    //stops the thred if called for by another
    public synchronized void stopRunning(){
        synchronized(command){
            command.cancel();
        }
        done = true;
    }


}
