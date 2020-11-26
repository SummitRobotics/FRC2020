/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.devices.LEDs;

import frc.robot.utilities.functionalinterfaces.LEDControlFunction;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


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




    // private Map<String, LEDCall> calls;
    // private LEDs leds;
    // private String lastKey;
    // private int callCalls;
    

    // /**
    //  * makes a new led range
    //  * @param leds the leds device
    //  * @param ranges the ranges of leds to use eg[0, 10]
    //  */
    // public LEDRange(int firstLed, int lastLed){
    //     this.ranges = ranges;
    //     this.leds = leds;
    //     calls = new HashMap<>();
    // }

    // /**
    //  * adds a new led call if it dosent already exist
    //  * @param name name of the call so it can be removed
    //  * @param call the led call object to add
    //  */
    // public void addLEDCall(String name, LEDCall call){
    //     calls.putIfAbsent(name, call);
    // }

    // /**
    //  * removes a led call
    //  * @param name name of call to remove
    //  */
    // public void removeLEDCall(String name){
    //     if(name == lastKey){
    //         callCalls = 0;
    //     }
    //     calls.remove(name);
    // }

    // /**
    //  * sets the color of the ranges with the control function 
    //  * @param controlFunction led control function to determen the color of the leds
    //  * @param call the call number to pass into the controlFunction
    //  */
    // private void setColor(LEDControlFunction controlFunction, int call){
    //     for (int[] x : ranges){
    //         int y = x[0];
    //         for(y=y; y < x[1]; y++){
    //             int[] color = controlFunction.getLedColor(call, y);
    //             leds.setLED(color[0], color[1], color[2], y);
    //         }
            
    //     }
    //     leds.applyChanges();
        
    // }

    // //does all the things
    // @Override
    // public void periodic() {
    //     String keyWithHighestPriority = null;
    //     int HighestPriority = 0;
    //     //finds the call with the highest priority
    //     for (Map.Entry<String, LEDCall> entry : calls.entrySet()){
    //         int priority = entry.getValue().getPriority();
    //         if(priority > HighestPriority){
    //             keyWithHighestPriority = entry.getKey();
    //             HighestPriority = priority;
    //         }
    //     }
    //    if (keyWithHighestPriority == null){
    //        return;
    //    }
    //    else{
    //        if(keyWithHighestPriority != lastKey){
    //            callCalls = 0;
    //            lastKey = keyWithHighestPriority;
    //        }
    //        callCalls++;
    //        setColor(calls.get(lastKey).getControlFunction(), callCalls);
    //    }
    // }
}
