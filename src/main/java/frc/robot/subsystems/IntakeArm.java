/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;

/**
 * Add your docs here.
 */
public class IntakeArm extends SubsystemBase implements Logger {

  //TODO - make pid right
  private static final double
  P = 0,
  I = 0,
  D = 0;

  private CANSparkMax intake = new CANSparkMax(Ports.INTAKE_ARM_INTAKE.port, MotorType.kBrushless);
  private CANSparkMax pivot = new CANSparkMax(Ports.INTAKE_ARM_PIVOT.port, MotorType.kBrushless);

  private CANEncoder pivotEncoder = new CANEncoder(pivot);
  
  private CANPIDController pivotPID = pivot.getPIDController();

  //data for logger
  private double pivotPower = 0, intakePower = 0;

  private int
  //PID Config
  PID_OUTPUT_MIN = -1,
  PID_OUTPUT_MAX = 1;

  public IntakeArm(){
    pivotEncoder.setPosition(0);
    pivotPID.setP(P);
    pivotPID.setI(I);
    pivotPID.setD(D);
    pivotPID.setOutputRange(PID_OUTPUT_MIN, PID_OUTPUT_MAX);
  }

  @Override
  public double[] getValues(double[] values) {
    values[LoggerRelations.INTAKE_ARM_INTAKE_POWER.value] = intakePower;
    values[LoggerRelations.INTAKE_ARM_PIVOT_POWER.value] = pivotPower;
    values[LoggerRelations.INTAKE_ARM_PIVOT_POSITION.value] = getPivotEncoderPosition();
    return values;
  }

  public void setPivotPower(double power){
    power = Functions.clampDouble(power, 1, -1);
    pivotPower = power;
    pivot.set(power);
  }

  public void setIntakePower(double power){
    power = Functions.clampDouble(power, 1, -1);
    intakePower = power;
    intake.set(power);
  }

  public void setPivotEndoerPosition(double position){
    pivotEncoder.setPosition(position);
  }

  public double getPivotEncoderPosition(){
    return pivotEncoder.getPosition();
  }

  public void setPivotMotorPosition(double position){
    pivotPID.setReference(position, ControlType.kPosition);
  }

  public void stop(){
    intake.set(0);
    pivot.set(0);
  }
}
