/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

/**
 * Add your docs here.
 */
public class shufHELLboardDriver {

    //THESE NEED TO BE STATIC TO WORK RIGHT!!!! TURST ME!!!!!! THE ROBOT WILL CRASH ON THE SECOND INIT IF THEY ARE NOT STATIC!!!!!!!!!!!!!!!!!!!
    public static shufHELLboardButton
    recordStart = new shufHELLboardButton(0, 0, "recordstart", "record"),
    intake = new shufHELLboardButton(1, 0, "intake", "record"),
    shoot = new shufHELLboardButton(2, 0, "shooter", "record"),
    shift = new shufHELLboardButton(3, 0, "shift", "record"),
    finish = new shufHELLboardButton(6, 0, "finish", "record");



    public shufHELLboardDriver(){
        
    }

}
