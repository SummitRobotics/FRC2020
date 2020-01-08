/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.logging.Logger;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Constants.LoggerRelations;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.sensors.PigeonIMU;

public class IMU extends SubsystemBase implements Logger{

  private PigeonIMU gyro = new PigeonIMU(Constants.PIGEON_IMU);
  private double xGforce = 0, yAngle = 0, heading = 0; 

  @Override
  public double[] getValues(double[] values){
      values[LoggerRelations.IMU_X_GFORCE.value] = xGforce;
      values[LoggerRelations.IMU_Y_ANGLE.value] = yAngle;
      values[LoggerRelations.IMU_HEADING.value] = heading;

    return values;
  }


  /**
   * Creates a new IMU.
   */
  public IMU() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

      /**
     * gets the g-force experienced by the robot forwards and backwards
     * @return the g-force experienced by the robot's x axis
     */
    public double getXGforce(){
      short[] axies = {0,0,0};
      gyro.getBiasedAccelerometer(axies);
      double out = (((double)(axies[0]))/16384.0);
      xGforce = out;
      return out;
  }

  /**
   * gets the angle that the robot is tipped forwards and backwards
   * @return y axis angle
   */
  public double getYAngle(){
      double[] angles = {0,0,0};
      gyro.getYawPitchRoll(angles);
      double out = angles[2];
      yAngle = out;
      return out;
  }

  /**
   * gets the robots current heading
   * @return the current heading in degrees
   */
  public double getHeading(){
    double out = gyro.getFusedHeading();
    heading = out;
    return out;
  }

  /**
   * sets the current position to be the input
   * @param setpoint the value you want the current orientation to read
   */
  public void setHeading(double setpoint){
      gyro.setFusedHeading(setpoint);
  }

}
