/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.lists.Ports;

public class Pneumatics extends SubsystemBase {
  
  private Compressor compressor;

  private AnalogInput pressureSensor;

  private NetworkTableEntry entry;

  public Pneumatics(NetworkTableEntry entry) {
    this.entry = entry;
    compressor = new Compressor(Ports.PCM_1, PneumaticsModuleType.CTREPCM);
    compressor.enableDigital();
    pressureSensor = new AnalogInput(Ports.PRESSURE_SENSOR);

    entry.forceSetNumber(getPressure());
  }

  public double getPressure(){
    return (250 * (pressureSensor.getAverageVoltage() / 5)) - 25;
  }

  // public void setClosedLoopControl(boolean set){
  //   compressor.setClosedLoopControl(set);
  // }

  public boolean getPressureSwitch(){
    return compressor.getPressureSwitchValue();
  }

  public void setCompressor(boolean set){
    if(set){
      compressor.enableDigital();
    }
    else{
      compressor.disable();
    }
  }

  @Override
  public void periodic() {
    entry.forceSetNumber(getPressure());
  }
}
