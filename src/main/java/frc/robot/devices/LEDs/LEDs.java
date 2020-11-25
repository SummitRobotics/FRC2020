/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.devices.LEDs;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.robot.utilities.Ports;

/**
 * Device to manage LEDs
 */
public class LEDs {

    private AddressableLED ledStrip;
    private AddressableLEDBuffer buffer;

    public LEDs() {
        ledStrip = new AddressableLED(Ports.LED_PORT);
        buffer = new AddressableLEDBuffer(Ports.LED_LENGTH);

        ledStrip.setLength(Ports.LED_LENGTH);
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
    public void setLED(int r, int g, int b, int led){
            buffer.setRGB(led, r, g, b);
    }

    /**
     * applies the changes made by setLed to the strip
     */
    public void applyChanges(){
        ledStrip.setData(buffer);
    }



}
