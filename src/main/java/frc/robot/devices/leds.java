/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.devices;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
/**
 * Add your docs here.
 */
public class leds{
    //pwm port the strip is pluged into
    private final int port = 9;
    //the length of the
    private final int length = 58;
    private AddressableLED ledStrip;
    private AddressableLEDBuffer buffer;

    public leds(){
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
    private void setMultipulRanges(int r, int g, int b, ledRange... ranges){
        for(ledRange x : ranges){
            x.setColor(r, g, b);
        }
    }


    /**
     * interface of a led range controler
     */
    public interface ledRange {
        public void setColor(int r, int g, int b);
    }
}
