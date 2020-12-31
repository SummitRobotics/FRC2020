/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * Add your docs here.
 */
public class HoodIndicatorWidget {

    private NetworkTableEntry entry;

    public HoodIndicatorWidget(NetworkTableEntry entry){

        this.entry = entry;
        entry.forceSetDouble(0);
    }

    /**
     * @param Value the value in dagrees of the hood from the lower hardstop
     */
    public void SetValue(double Value){
        entry.setDouble(Value);
    }
}
