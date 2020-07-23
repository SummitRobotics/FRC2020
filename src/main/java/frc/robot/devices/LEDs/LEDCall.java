/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.devices.LEDs;

import frc.robot.utilities.functionalinterfaces.LEDControlFunction;


public class LEDCall {
    private int priority;
    private LEDControlFunction controlFunction;
    public LEDCall(int priority, LEDControlFunction controlFunction){
        this.priority = priority;
        this.controlFunction = controlFunction;

    }

    public static LEDControlFunction solid(int[] color){
        return (a, b) -> {return color;};
    }

    // add fun dynamic ones later

    public int getPriority(){
        return priority;
    }

    public LEDControlFunction getControlFunction(){
        return controlFunction;
    }
}