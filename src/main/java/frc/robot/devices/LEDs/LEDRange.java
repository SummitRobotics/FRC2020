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


public class LEDRange extends SubsystemBase{

    private int[][] ranges;
    private Map<String, LEDCall> calls;
    private LEDs leds;
    private String lastKey;
    private int callCalls;
    

    public LEDRange( LEDs leds, int[]... ranges){
        this.ranges = ranges;
        this.leds = leds;
        calls = new HashMap<>();
    }

    public void addLEDCall(String name, LEDCall call){
        calls.putIfAbsent(name, call);
    }

    public void removeLEDCall(String name){
        if(name == lastKey){
            callCalls = 0;
        }
        calls.remove(name);
    }

    private void setColor(LEDControlFunction controlFunction, int call){
        for (int[] x : ranges){
            for(int y = x[0]; y > x[1]; y++){
                int[] color = controlFunction.getLedColor(call, y);
                leds.setLED(color[0], color[1], color[2], y);
            }
        }
        leds.applyChanges();
        
    }

    @Override
    public void periodic() {
        String keyWithHighestPriority = null;
        int HighestPriority = 0;
        for (Map.Entry<String, LEDCall> entry : calls.entrySet()){
            int priority = entry.getValue().getPriority();
            if(priority > HighestPriority){
                keyWithHighestPriority = entry.getKey();
                HighestPriority = priority;
            }
        }
       if (keyWithHighestPriority == null){
           return;
       }
       else{
           if(keyWithHighestPriority != lastKey){
               callCalls = 0;
               lastKey = keyWithHighestPriority;
           }
           callCalls++;
           setColor(calls.get(lastKey).getControlFunction(), callCalls);
       }
    }
}
