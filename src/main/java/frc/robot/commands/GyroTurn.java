/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import edu.wpi.first.wpilibj.controller.PIDController;
import frc.robot.devices.PigeonGyro;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Functions;

public class GyroTurn extends CommandBase {

  private PIDController pidController = new PIDController(Constants.GYRO_P, Constants.GYRO_I, Constants.GYRO_D);
  private Drivetrain drivetrain;
  private PigeonGyro gyro;

  private double setPoint, angle;

  private final double ACCEPATBLE_ERROR = .5;

  /**
   * turns with gyro
   * 
   * @param imu        imu instance
   * @param drivetrain drivetrain instance
   * @param angle      the angle you want to turn
   */
  public GyroTurn(PigeonGyro gyro, Drivetrain drivetrain, double angle) {
    this.gyro = gyro;
    this.drivetrain = drivetrain;
    this.angle = angle;

    // sets acceptable error
    pidController.setTolerance(ACCEPATBLE_ERROR);

    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // finds setpoint for pid controler
    setPoint = angle + gyro.getHeading();
    pidController.setSetpoint(setPoint);
    // sets ramprate to 0 so pid works right
    drivetrain.setOpenRampRate(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // gives pid controler values and gets back power that is then limited to .25
    double power = Functions.clampDouble(pidController.calculate(gyro.getHeading()), .25, -.25);
    System.out.println(power);
    // sets motor power to pid output
    drivetrain.setLeftMotorPower(-power);
    drivetrain.setRightMotorPower(power);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // if pid controler is at setpoint then finish command
    return pidController.atSetpoint();
  }
}
