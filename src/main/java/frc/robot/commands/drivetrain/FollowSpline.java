// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain;

import java.util.List;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;

//this is REAL bad
public class FollowSpline extends CommandBase {

  private TrajectoryConfig config;

  private Trajectory trajectory;
  
  private Drivetrain drivetrain;

  private RamseteCommand command;

  public FollowSpline(Drivetrain drivetrain) {
    this.drivetrain = drivetrain;

    double[] pid  = drivetrain.getPid();

    //TODO make numbers right
    config = new TrajectoryConfig(2,1)
    // Add kinematics to ensure max speed is actually obeyed
    .setKinematics(drivetrain.driveKinimatics)
    // Apply the voltage constraint
    .addConstraint(drivetrain.VoltageConstraint);

    trajectory = TrajectoryGenerator.generateTrajectory(
      // Start at the origin facing the +X direction
      new Pose2d(0, 0, new Rotation2d(0)),
      // Pass through these two interior waypoints, making an 's' curve path
      List.of(
          new Translation2d(1, 1),
          new Translation2d(2, -1)
      ),
      // End 3 meters straight ahead of where we started, facing forward
      new Pose2d(3, 0, new Rotation2d(0)),
      // Pass config
      config
  );

    //this is biggest sin
    this.command = new RamseteCommand(
      trajectory, 
      drivetrain::getPose, 
      //TODO make right
      new RamseteController(1, .5), 
      drivetrain.feedFoward, 
      drivetrain.driveKinimatics, 
      drivetrain::getWheelSpeeds, 
      new PIDController(pid[0], pid[1], pid[2], pid[3]), 
      new PIDController(pid[0], pid[1], pid[2], pid[3]), 
      drivetrain::setMotorVolts, 
      drivetrain);
  }

  @Override
  public void initialize() {
    drivetrain.setPose(trajectory.getInitialPose());
    command.initialize();
  }

  @Override
  public void execute() {
    command.execute();
  }

  @Override
  public void end(boolean interrupted) {
    command.end(interrupted);
    drivetrain.setMotorVolts(0, 0);
  }

  @Override
  public boolean isFinished() {
    return command.isFinished();
  }

}
