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
  private ControllerDriver controller;

  private double turn = 0;
  private int index = 0;
  //last 8 turn inputs from controler
  private double[] turns = new double[8];


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

    //turning stuff
    double stick = controller.leftStickX();
    //loops through array
    if(index == turns.length){
      index = 0;
    }
    //adds current stick value / by array length to array. devided so if stick is pussed fully one way for 8 loops than the arrays sum will be 1
    turns[index] = stick/turns.length;
    //increments index
    index++;
    //makes it so turnin is easyer beacuse shorter controler inputs will have less effect
    if(stick == 0){
      turn = 0;
    }
    else{
      for(int i = 0; i<turns.length; i++){
        turn += turns[i];
      }
    }
    
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
