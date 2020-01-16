/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.Lemonlight;
import frc.robot.livepid.LivePIDController;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.Constants;
import edu.wpi.first.wpilibj.controller.PIDController;

public class VisionTrack extends CommandBase {

  private Lemonlight lemonlight;
  private Turret turret;
  private LivePIDController pidController = new LivePIDController("Turret", Constants.TURRET_P, Constants.TURRET_I, Constants.TURRET_D);

  /**
   * Creates a new VisionTrack.
   */
  public VisionTrack(Lemonlight ll, Turret t) {
    lemonlight = ll;
    turret = t;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pidController.setSetpoint(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      if(!lemonlight.hasTarget()){
        pidController.reset();
      }
      pidController.update();
      double in = lemonlight.getHorizontalOffset();
      //System.out.println(in);
      double power = pidController.calculate(in);
      //System.out.println(power);
      turret.setPower(power);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
