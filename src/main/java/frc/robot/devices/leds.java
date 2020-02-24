/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.devices;

import java.util.HashMap;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
/**
 * Add your docs here.
 */
public class LEDs extends SubsystemBase {

    private final int port = 9; //pwm port the strip is pluged into
    private final int length = 29; //the length of the strip

    private AddressableLED ledStrip;
    private AddressableLEDBuffer buffer;

    private HashMap<String, LEDBlinker> blinkers;

    public LEDs(){
        ledStrip = new AddressableLED(port);
        buffer = new AddressableLEDBuffer(length);
        blinkers = new HashMap<>();

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
    public void changeLEDRange(int r, int g, int b, int start, int end){
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
    public void changeAllLEDs(int r, int g, int b){
        changeLEDRange(r, g, b, 0, length-1);
    }

    /**
     * gets a controler to controle a range of leds
     * @param start led to start at
     * @param end led to end at
     * @return usable led range controler
     */
    public LEDRange getRangeController(int start, int end){
        return (Color8Bit color) -> changeLEDRange(color.red, color.green, color.blue, start, end);
    }

        /**
     * gets a controler to controle all leds
     * @return usable led range controler
     */
    public LEDRange getAllLedsRangeController(){
        return (Color8Bit color) -> changeLEDRange(color.red, color.green, color.blue, 0, length-1);
    }

    /**
     * used to group multipul led ranges into a single range
     * @param ranges the ranges to be grouped
     * @return the new grouped controler
     */
    public LEDRange groupRangeControllers(LEDRange... ranges){
        return (Color8Bit color) -> setMultipleRanges(color, ranges);
    }

    /**
     * used to set multipul ranges at a time
     * @param r red 0-255
     * @param g green 0-255
     * @param b blue 0-255
     * @param ranges the ranges to be set
     */
    private void setMultipleRanges(Color8Bit color, LEDRange... ranges) {
        for(LEDRange x : ranges){
            x.setColor(color);
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
    public void addBlinker(String name, LEDRange range, int period, Color8Bit... colors){
        blinkers.put(name, new LEDBlinker(range, period, colors));
    }

    public void removeBlinker(String name){
        blinkers.remove(name);
    }

    /**
     * used to make leds blink
     */
    @Override
    public void periodic(){
        blinkers.forEach((k, v) -> v.increment());
    }

    /**
     * interface of a led range controler
     */
    public interface LEDRange {
        public void setColor(Color8Bit color);
    }

    /**
     * blinker class to store all data for a blinker
     */
    public class LEDBlinker {
        private LEDRange range;
        private int period;
        private Color8Bit[] colors;

        private int count, position;

        public LEDBlinker(LEDRange range, int period, Color8Bit[] colors) {
            this.range = range;
            this.period = period;
            this.colors = colors;

            count = 0;
            position = 0;
        }

        public void increment() {
            count = (count + 1) % period;

            if (count == 0) {
                position = (position + 1) % colors.length;
                range.setColor(colors[position]);
            }
        }
    }
}
