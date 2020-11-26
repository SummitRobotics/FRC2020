/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.devices.LEDs;

import java.util.Collections;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Colors;
import frc.robot.utilities.Ports;
import java.util.HashMap;

import frc.robot.utilities.functionalinterfaces.LEDControlFunction;

/**
 * Device to manage LEDs
 */
public class LEDs extends SubsystemBase {

    private AddressableLED ledStrip;
    private AddressableLEDBuffer buffer;
    private HashMap<String, LEDCall> calls;

    HashMap<Integer, HashMap<Integer,LEDControlFunction>> hell;

    HashMap<Integer, LEDControlFunction> ledToFunction;

    private int loops;

    private boolean listrebiuld;

    public LEDs() {
        listrebiuld = true;
        loops = 0;
        ledStrip = new AddressableLED(Ports.LED_PORT);
        buffer = new AddressableLEDBuffer(Ports.LED_LENGTH);

        calls = new HashMap<>();
        hell = new HashMap<>();
        ledToFunction = new HashMap<>();

        ledStrip.setLength(Ports.LED_LENGTH);
        ledStrip.start();


    }

    /**
     * changes a range of leds
     * @param r red 0-255
     * @param g green 0-255
     * @param b blue 0-255
     * @param led the led to change
     */
    private void setLED(int[] color, int led){
            buffer.setRGB(led, color[0], color[1], color[2]);
    }

    /**
     * applies the changes made by setLed to the strip
     */
    private void applyChanges(){
        ledStrip.setData(buffer);
    }

    @Override
    public void periodic() {
        loops++;

        //this is bad and uses lots of ram probably

        if(listrebiuld){
            hell.clear();
            ledToFunction.clear();
            
            //goes through all calls
            for(LEDCall call : calls.values()){
                //gets the range for each call
                int[][] range = call.getLedRange().getRange();
                //goes through all ranges of leds in a range
                for(int[] leds : range){
                    //goes through a range of leds
                    for(int i = leds[0]; i <= leds[1]; i++){
                        //if there is no entry for a led make one and add the hashmap of the priority and a call to it
                        if(hell.get(i) == null){
                            HashMap<Integer,LEDControlFunction> priorityToFunction = new HashMap<>();
                            priorityToFunction.put(call.getPriority(), call.getControlFunction());
                            hell.put(i, priorityToFunction);
                        }
                        //if there is already an entry add the new call and priority to it
                        else{
                            hell.get(i).put(call.getPriority(), call.getControlFunction());
                        }
                    }
                }
            }

            //loops through all leds in the strip
            for(int x = 0; x<Ports.LED_LENGTH; x++){
                var priAndFunc = hell.get(x);
                //if there is no function for a led than add one to make it off
                if(priAndFunc == null){
                    ledToFunction.put(x, LEDCall.solid(Colors.Off));
                }
                //finds the control function with the highest priority and put that in the map for the led
                else{
                    int maxValueInMap=(Collections.max(priAndFunc.keySet()));
                    ledToFunction.put(x, priAndFunc.get(maxValueInMap));
                }
            }

            //makes it so we dont do all of this if we dont need to
            listrebiuld = false;
        }

        //sets the led to the color based on the function for that led
        for(int x = 0; x < Ports.LED_LENGTH; x++){
            setLED(ledToFunction.get(x).getLedColor(loops, x), x);
        }

        
        applyChanges();
    }



    /**
     * adds a new led call if it dosent already exist
     * @param name name of the call so it can be removed
     * @param call the led call object to add
     */
    public void AddCall(String name, LEDCall call){
        listrebiuld = true;
        calls.put(name, call);
    }

    /**
     * removes a led call
     * @param name name of call to remove
     */
    public void RemoveCall(String name){
        listrebiuld = true;
        calls.remove(name);
    }



}
