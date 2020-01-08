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

  private Drivetrain drivetrain;
  private ControllerDriver cDriver;

  /**
   * telyop driver control
   * @param drivetrain drivetrain instance
   * @param cDriver controler instance
   */
  public ArcadeDrive(Drivetrain drivetrain, ControllerDriver cDriver) {
    this.drivetrain = drivetrain;
    this.cDriver = cDriver;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double power = cDriver.rightTrigger() - cDriver.leftTrigger();
    double leftPower = power + cDriver.leftStickX();
    double rightPower = power - cDriver.leftStickX();

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
