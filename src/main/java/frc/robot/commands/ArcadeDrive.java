/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.oi.ControllerDriver;

public class ArcadeDrive extends CommandBase {

  private final Drivetrain drivetrain;
  private final ControllerDriver controller;

  /**
   * teleop driver control
   * @param drivetrain drivetrain instance
   * @param controller controller instance
   */
  public ArcadeDrive(final Drivetrain drivetrain, final ControllerDriver controller) {
    this.drivetrain = drivetrain;
    this.controller = controller;

    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.setOpenRampRate(.3);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double power = controller.rightTrigger() - controller.leftTrigger();
    double turn = controller.leftStickX()*.5;
    
    double leftPower = power + turn;
    double rightPower = power - turn;
    if (turn != 0 && power < .05) {
      drivetrain.setOpenRampRate(0);
    } else {
      drivetrain.setOpenRampRate(.5);
    }
    drivetrain.setLeftMotorPower(leftPower);
    drivetrain.setRightMotorPower(rightPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    drivetrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
