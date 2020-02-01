/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shifter;
import frc.robot.oi.ControllerDriver;

public class ArcadeDrive extends CommandBase {

  private Drivetrain drivetrain;
  private ControllerDriver controller;
  private Shifter shift;
  private final double deadzone = .1;

  private double old = 0;
  //max amount motors can change per 20 ms
  private final double max_change_rate = .15;

  /**
   * teleop driver control
   * @param drivetrain drivetrain instance
   * @param controller controller instance
   * @param shift shifter instance
   */
  public ArcadeDrive(
    Drivetrain drivetrain, ControllerDriver controller, Shifter shift) {
    this.drivetrain = drivetrain;
    this.controller = controller;
    this.shift = shift;

    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.setOpenRampRate(0);
    shift.Shift(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double power = controller.rightTrigger() - controller.leftTrigger();

    double turn = controller.leftStickX();

    //turn deadzone
    if (Math.abs(turn) < deadzone){
      turn = 0;
    }

    //power rate of change
      if(power > old+max_change_rate){
        power = old+max_change_rate;
        old = power;
      }
      else if(power < old-max_change_rate){
          power = old-max_change_rate;
          old = power;
      }
      else{
        old = power;
      }
  
      //calculates power to the motors
    double leftPower = power + turn;
    double rightPower = power - turn;

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
