// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain;

import java.time.Period;
import java.util.List;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

//this is REAL bad
public class FollowSpline extends CommandBase {

    private TrajectoryConfig config;
    private Trajectory trajectory;
    private Drivetrain drivetrain;

    private RamseteCommand command;

    public FollowSpline(Drivetrain drivetrain) {
        super();

        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        double[] pid = drivetrain.getPid();

        config = new TrajectoryConfig(2, 2)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(drivetrain.DriveKinimatics)
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

        super.initialize();
    }

    @Override
    public void execute() {
        super.execute();

        command.execute();
    }

    @Override
    public boolean isFinished() {
        return command.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);

        if (interrupted) {
            command.cancel();
        }
    }
}
