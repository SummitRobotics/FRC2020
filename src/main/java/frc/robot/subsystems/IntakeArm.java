/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;

/**
 * Add your docs here.
 */
public class IntakeArm extends SubsystemBase {

  private CANSparkMax intake = new CANSparkMax(Ports.INTAKE_ARM_INTAKE.port, MotorType.kBrushless);
  private CANSparkMax pivot = new CANSparkMax(Ports.INTAKE_ARM_PIVOT.port, MotorType.kBrushless);

  private CANEncoder pivotEncoder = new CANEncoder(pivot);

  public IntakeArm(){
    pivotEncoder.setPosition(0);
  }

  public void setPivotPower(double power){
    power = Functions.clampDouble(power, 1, -1);
    pivot.set(power);
  }

  public void setIntakePower(double power){
    power = Functions.clampDouble(power, 1, -1);
    intake.set(power);
  }

  public void setPivotEndoerPosition(double position){
    pivotEncoder.setPosition(position);
  }

  public double getPivotEncoderPosition(){
    return pivotEncoder.getPosition();
  }

  public void stop(){
    intake.set(0);
    pivot.set(0);
  }
}
