/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.logging.Logger;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Functions;

/**
 * Add your docs here.
 */
public class Conveyor extends SubsystemBase implements Logger{

  private double leftConveyorMotorPower = 0, rightConveyorMotorPower = 0;

  private CANSparkMax leftConveyor = new CANSparkMax(Constants.CONVEYOR_LEFT, MotorType.kBrushless);
  private CANSparkMax rightConveyor = new CANSparkMax(Constants.CONVEYOR_RIGHT, MotorType.kBrushless);

  private DigitalInput breakbeamEnter = new DigitalInput(Constants.BREAKBEAM_ENTER);
  private DigitalInput breakbeamExit = new DigitalInput(Constants.BREAKBEAM_EXIT);

  public Conveyor(){
  }

  @Override
  public double[] getValues(double[] values) {
    values[Constants.LoggerRelations.CONVEYOR_LEFT_POWER.value] = leftConveyorMotorPower;
    values[Constants.LoggerRelations.CONVEYOR_RIGHT_POWER.value] = rightConveyorMotorPower;
    values[Constants.LoggerRelations.CONVEYOR_BREAKBEAM_ENTER.value] = getBreakBeamEnter() ? 1 : 0;
    values[Constants.LoggerRelations.CONVEYOR_BREAKBEAM_EXIT.value] = getBreakBeamExit() ? 1 : 0;
    return values;
  }

  public void setLeftConveyor(double power){
    power = Functions.clampDouble(power, 1, -1);
    leftConveyorMotorPower = power;
    leftConveyor.set(power);
  }

  public void setRightConveyor(double power){
    power = Functions.clampDouble(power, 1, -1);
    rightConveyorMotorPower = power;
    rightConveyor.set(power);
  }

  public boolean getBreakBeamEnter(){
    return breakbeamEnter.get();
  }

  public boolean getBreakBeamExit(){
    return breakbeamExit.get();
  }

  public void stop(){
    leftConveyor.set(0);
    rightConveyor.set(0);
  }

}
