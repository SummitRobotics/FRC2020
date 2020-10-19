/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utilities;

/**
 * Add your docs here.
 */
public class ButtonOnlyTureOncePerPress {

    private boolean last;

    public ButtonOnlyTureOncePerPress(){
        last = false;
    }

    //returns ture on the first time true is passed in and wolnt ruturn true agin until its state is false agin and the true
    public boolean get(boolean curentState){
        if(last != curentState){
            last = curentState;
            return curentState;
        }
        else{
            return false;
        }
    }
}
