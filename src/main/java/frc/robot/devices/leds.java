/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.devices;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
/**
 * Add your docs here.
 */
public class leds extends SubsystemBase{
    //pwm port the strip is pluged into
    private final int port = 9;
    //the length of the
    private final int length = 58;
    private AddressableLED ledStrip;
    private AddressableLEDBuffer buffer;
    private int loops = 0;
    private ArrayList<ledBlinker> blinkers = new ArrayList<>();

    public leds(){
        CommandScheduler.getInstance().registerSubsystem(this);
        ledStrip = new AddressableLED(port);
        buffer = new AddressableLEDBuffer(length);
        ledStrip.setLength(length);
        ledStrip.start();
    }

    /**
     * changes a range of leds
     * @param r red 0-255
     * @param g green 0-255
     * @param b blue 0-255
     * @param start led to start change at
     * @param end led to end change at
     */
    public void changeLedRange(int r, int g, int b, int start, int end){
        for(int i = start; i<=end; i++){
            buffer.setRGB(i, r, g, b);
        }
        ledStrip.setData(buffer);
    }

    /**
     * changes all leds
     * @param r red 0-255
     * @param g green 0-255
     * @param b blue 0-255
     */
    public void changeAllLeds(int r, int g, int b){
        changeLedRange(r, g, b, 0, length-1);
    }

    /**
     * gets a controler to controle a range of leds
     * @param start led to start at
     * @param end led to end at
     * @return usable led range controler
     */
    public ledRange getRangeControler(int start, int end){
        return (int r, int g, int b) -> changeLedRange(r, g, b, start, end);
    }

    /**
     * used to group multipul led ranges into a single range
     * @param ranges the ranges to be grouped
     * @return the new grouped controler
     */
    public ledRange groupRangeControlers(ledRange... ranges){
        return (int r, int g, int b) -> setMultipulRanges(r, g, b, ranges);
    }

    /**
     * used to set multipul ranges at a time
     * @param r red 0-255
     * @param g green 0-255
     * @param b blue 0-255
     * @param ranges the ranges to be set
     */
    private void setMultipulRanges(int r, int g, int b, ledRange... ranges) {
        for(ledRange x : ranges){
            x.setColor(r, g, b);
        }
    }

    /**
     * adds a ledrange to be blinked
     * @param range the range to blink
     * @param period the multipul of 20ms the leds should blink at
     * @param name the name of the blinker
     * @param r1 red in state 1 0-255
     * @param g1 green in state 1 0-255
     * @param b1 blue in state 1 0-255
     * @param r2 red in state 2 0-255
     * @param g2 green in state 2 0-255
     * @param b2 blue in state 2 0-255
     */
    public void addBlinker(ledRange range, int period, String name, int r1, int g1, int b1, int r2, int g2, int b2){
        ledBlinker led = new ledBlinker();
        led.name = name;
        led.period = period;
        led.leds = range;
        led.r1 = r1;
        led.g1 = g1;
        led.b1 = b1;
        led.r2 = r2;
        led.g2 = g2;
        led.b2 = b2;
        blinkers.add(led);
    }

    public void removeBlinker(String name){
        blinkers.removeIf(x -> (x.name.equals(name)));
    }


    /**
     * used to make leds blink
     */
    @Override
    public void periodic(){
        loops++;
        for (ledBlinker x : blinkers){
            if(x.period % loops == 0){
                if(x.state){
                    x.leds.setColor(x.r1, x.g1, x.b1);
                    x.state  = !x.state;
                }
                else{
                    x.leds.setColor(x.r2, x.g2, x.b2);
                    x.state  = !x.state;
                }
            }
        }
    }


    /**
     * interface of a led range controler
     */
    public interface ledRange {
        public void setColor(int r, int g, int b);
    }

    /**
     * blinker class to store all data for a blinker
     */
    public class ledBlinker{
        public boolean state = false;
        public int period;
        public ledRange leds;
        public String name; 
        public int r1;
        public int g1;
        public int b1;
        public int r2;
        public int g2;
        public int b2;
    }
}
