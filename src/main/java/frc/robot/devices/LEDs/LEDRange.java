/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.devices.LEDs;


public enum LEDRange{

    LeftClimb(new int[][] {{34,57}}),
    Mid(new int[][] {{24, 33}}),
    RightClimb(new int[][] {{0, 23}}),
    BothClimb(new int[][] {{34,57},{0, 23}}), 
    LeftIntake(new int[][] {{58, 76}}),
    RightIntake(new int[][] {{77, 96}}),
    BothIntake(new int[][] {{58, 76},{77, 96}}),
    AllLeft(new int[][] {{58, 76},{34,57}}), 
    AllRight(new int[][] {{77, 95},{0, 23}}),
    All(new int[][] {{0,95}});

    public int[][] range;

    LEDRange(int[][] range) {
        this.range = range;
    }

    public int[][] getRange() {
        return this.range;
     }

}
