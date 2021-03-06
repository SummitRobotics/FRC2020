// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.Functions;
import frc.robot.utilities.SerialisableMultiGearTrejectory;

//this is REAL bad
public class FollowSavedTrejectory extends CommandBase {

    private Trajectory trajectory;
    private Drivetrain drivetrain;
    private String path;

    private RamseteCommand command;

    public FollowSavedTrejectory(Drivetrain drivetrain, String path) {
        super();

        this.path = path;
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        double[] pid = drivetrain.getPid();

        // scaled by 3 for testing so i dont break my walls
        try{
            SerialisableMultiGearTrejectory both = Functions.RetriveObjectFromFile(path);
            trajectory = both.getTrajectory(drivetrain.getShift());
        }
        catch(Exception e){
            //should cause a crash
            end(true);
        }

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
