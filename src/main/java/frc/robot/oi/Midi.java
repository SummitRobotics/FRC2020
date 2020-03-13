/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
/**
 * Add your docs here.
 */
public class Midi {

    public NetworkTable midiNetTable;

    public Midi(){
        NetworkTableInstance netTableInstance = NetworkTableInstance.getDefault();
        midiNetTable = netTableInstance.getTable("midi");
    }
}
