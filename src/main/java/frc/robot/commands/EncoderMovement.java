/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drivetrain;

public class EncoderMovement implements Command {

  private Drivetrain drivetrain;

  private double leftDistance = 0, rightDistance = 0;
  private double leftTarget = 0, rightTarget = 0;
  private boolean finished = false;

  // acceptable error in motor rotations
  private final double ACCEPTABLE_ERROR = 1;

  /**
   * Creates a new EncoderMovement.
   */
  public EncoderMovement(Drivetrain drivetrain, double leftDistance, double rightDistance) {
    this.rightDistance = rightDistance;
    this.leftDistance = leftDistance;
    this.drivetrain = drivetrain;
  }
  
  @Override
  public Set<Subsystem> getRequirements() {
    Set<Subsystem> requirements = new HashSet<Subsystem>();
    requirements.add(drivetrain);
    return requirements;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // adds current encoder position to targets
    leftTarget = leftDistance + drivetrain.getLeftEncoderPosition();
    rightTarget = rightDistance + drivetrain.getRightEncoderPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.setLeftMotorPosition(leftTarget);
    drivetrain.setRightMotorPosition(rightTarget);

    // checks if both sides are under or equal to acceptable error and if they are
    // then finish the command
    if ((Math.abs(drivetrain.getLeftEncoderPosition() - leftTarget) <= ACCEPTABLE_ERROR)
        && (Math.abs(drivetrain.getRightEncoderPosition() - rightTarget) <= ACCEPTABLE_ERROR)) {
      finished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finished;
  }
}
