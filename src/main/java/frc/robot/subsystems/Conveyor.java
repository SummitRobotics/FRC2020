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
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;

/**
 * Add your docs here.
 */
public class Conveyor extends SubsystemBase implements Logger{

  private double leftConveyorMotorPower = 0, rightConveyorMotorPower = 0;

  private CANSparkMax leftConveyor = new CANSparkMax(Ports.CONVEYOR_LEFT.port, MotorType.kBrushless);
  private CANSparkMax rightConveyor = new CANSparkMax(Ports.CONVEYOR_RIGHT.port, MotorType.kBrushless);

  private DigitalInput breakbeamEnter = new DigitalInput(Ports.BREAKBEAM_ENTER.port);
  private DigitalInput breakbeamExit = new DigitalInput(Ports.BREAKBEAM_EXIT.port);

  public Conveyor(){
  }

  @Override
  public double[] getValues(double[] values) {
    values[LoggerRelations.CONVEYOR_LEFT.value] = leftConveyorMotorPower;
    values[LoggerRelations.CONVEYOR_RIGHT.value] = rightConveyorMotorPower;
    values[LoggerRelations.CONVEYOR_BREAKBEAM_ENTER.value] = getBreakBeamEnter() ? 1 : 0;
    values[LoggerRelations.CONVEYOR_BREAKBEAM_EXIT.value] = getBreakBeamExit() ? 1 : 0;
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
