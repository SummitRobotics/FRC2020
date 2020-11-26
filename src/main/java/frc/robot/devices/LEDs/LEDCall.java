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
    private LEDRange range;

    /**
     * led call to be added to a led range
     * @param priority the priority of the call, higher number = higher priority
     * @param controlFunction the function used to determen the color of the leds when the call is exicuted
     */
    public LEDCall(int priority, LEDControlFunction controlFunction, LEDRange range){
        this.priority = priority;
        this.controlFunction = controlFunction;
        this.range = range;

    }

    /**
     * makes all leds one color
     * @param color the color to make the leds
     * @return the function to do it
     */
    public static LEDControlFunction solid(int[] color){
        return (call, led) -> {return color;};
    }

    /**
     * makes the leds flash
     * @param onColor color for leds to change to
     * @param offColor color to make the leds when in other flash stare, probably black
     * @return the function to do it
     */
    public static LEDControlFunction flashing(int[] onColor, int[] offColor){
        return (call, led) -> {
            int time = call % 40;
            if(time < 20){
                return onColor;
            }
            else{
                return offColor;
            }
        };
    }

    /**
     * makes the leds flash twice then hold on the color
     * @param onColor color for leds to change to
     * @param offColor color to make the leds when in other flash stare, probably black
     * @return the function to do it
     */
    public static LEDControlFunction ffh(int[] onColor, int[] offColor){
        //turns on then off then on then off then stays on
        //each call incriment = ~20ms
        return (call, led) -> {
            if(call <= 5) {
                return onColor;
            }
            else if (call <= 10){
                return offColor;
            }
            else if(call <= 15){
                return onColor;
            }
            else if(call <= 20){
                return offColor;
            }
            else{
                return onColor;
            }
        };
    }

    /**
     * makes a scroling wave on the leds
     * @param color brightest color of the wave, all others will be dimmer
     * @return the function to do it
     */
    public static LEDControlFunction sine(int[] color){
        //use desmos to find this, should make wave land on 1/.5 on a whole number
        final double waveLength = 6.068;
        return (call, led) ->{
            //makes a wave to scale color values by going from 1 to .5
            double scaler = (Math.cos((((led*Math.PI)/waveLength)-call)+3))*.25;
            int[] out = new int[3];
            out[0] = (int)((color[0]*scaler)+.5);
            out[1] = (int)((color[1]*scaler)+.5);
            out[2] = (int)((color[2]*scaler)+.5);
            return out;
        };
    }

    /**
     * @return the priority of the call
     */
    public int getPriority(){
        return priority;
    }

    /**
     * @return the control function for the led call
     */
    public LEDControlFunction getControlFunction(){
        return controlFunction;
    }
    /**
     * @return the led range asociated with the call
     */
    public LEDRange getLedRange(){
        return range;
    }
}