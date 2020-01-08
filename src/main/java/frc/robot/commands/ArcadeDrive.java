/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drivetrain;
import frc.robot.oi.ControllerDriver;

public class ArcadeDrive implements Command {

  private Drivetrain drivetrain;
  private ControllerDriver controller;

  /**
   * teleop driver control
   * @param drivetrain drivetrain instance
   * @param controller controller instance
   */
  public ArcadeDrive(Drivetrain drivetrain, ControllerDriver controller) {
    this.drivetrain = drivetrain;
    this.controller = controller;
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return null;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double power = controller.rightTrigger() - controller.leftTrigger();
    double leftPower = power + controller.leftStickX();
    double rightPower = power - controller.leftStickX();
    System.out.println(leftPower);
    System.out.println(rightPower);
    drivetrain.setLeftMotorPower(leftPower);
    drivetrain.setRightMotorPower(rightPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
