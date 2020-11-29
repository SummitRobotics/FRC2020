/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.devices;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.utilities.Ports;

/**
 * Add your docs here.
 */
public class PresureSensor {

    AnalogInput pressureSensor;

    public PresureSensor(){
        pressureSensor = new AnalogInput(Ports.PRESSURE_SENSOR);
    }

    public double getPressure(){
        return (250 * (pressureSensor.getAverageVoltage() / 5)) - 25;
    }
}
