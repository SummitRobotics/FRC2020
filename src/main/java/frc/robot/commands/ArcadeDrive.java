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

  private double oldLeft = 0, oldRight = 0;
  //max amount motors can change per 20 ms
  private final double max_change_rate = .05;

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
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    drivetrain.setShotPower(controller.rightStickY());

    double power = controller.rightTrigger() - controller.leftTrigger();

    double turn = 0;
    //decreses turing power if in high gear
    if(shift.getShiftState()){
      turn = controller.leftStickX()*.5;
    }
    else{
      turn = controller.leftStickX()*.25;
    }
    
    double leftPower = power + turn;
    double rightPower = power - turn;

    //limits left power change rate
    if(leftPower > oldLeft+max_change_rate){
      leftPower = oldLeft+max_change_rate;
      drivetrain.setLeftMotorPower(leftPower);
      oldLeft = leftPower;
    }
    else if(leftPower < oldLeft-max_change_rate){
        leftPower = oldLeft-max_change_rate;
        drivetrain.setLeftMotorPower(leftPower);
        oldLeft = leftPower;
    }
    else{
      drivetrain.setLeftMotorPower(leftPower);
      oldLeft = leftPower;
    }
    
    //limits right power change rate
    if(rightPower > oldRight+max_change_rate){
      rightPower = oldRight+max_change_rate;
      drivetrain.setRightMotorPower(rightPower);
      oldRight = rightPower;
    }
    else if(rightPower < oldRight-max_change_rate){
      rightPower = oldRight-max_change_rate;
        drivetrain.setRightMotorPower(rightPower);
        oldRight = rightPower;
    }
    else{
      drivetrain.setRightMotorPower(rightPower);
      oldRight = rightPower;
    }  
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
