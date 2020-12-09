/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

/**
 * Driver for virtual shuffleboard buttons
 */
public class shufHELLboardDriver {

    // Variables refering to shuffleboard need to be static for reasons
    public static shufHELLboardButton
    recordStart = new shufHELLboardButton(0, 0, "recordstart", "record"),
    intake = new shufHELLboardButton(1, 0, "intake", "record"),
    shoot = new shufHELLboardButton(2, 0, "shooter", "record"),
    shift = new shufHELLboardButton(3, 0, "shift", "record"),
    finish = new shufHELLboardButton(6, 0, "finish", "record"),

    homeTurret = new shufHELLboardButton(0, 0, "home turret", "homing"),
    homeHood = new shufHELLboardButton(1, 0, "home hood", "homing");
}
