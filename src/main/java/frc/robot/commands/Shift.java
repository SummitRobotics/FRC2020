/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shifter;

public class Shift extends CommandBase {

  private Shifter shifter;

  private boolean finished = false;
  private boolean shiftState;

  /**
   * Creates a new Shift.
   */
  public Shift(Shifter shifter) {
    this.shifter = shifter;
    shiftState = !shifter.getShiftState();
  }

  /**
   * shifts to a prrticular state
   * @param shifter instance of shifter
   * @param state true is slow, false is fast
   */
  public Shift(Shifter shifter, boolean state) {
    this.shifter = shifter;
    shiftState = state;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shifter.Shift(shiftState);
    finished = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finished;
  }
}
