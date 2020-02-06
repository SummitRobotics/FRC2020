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
public class leds {
    private final int port = 9;
    private final int length = 58;
    private AddressableLED ledStrip;
    private AddressableLEDBuffer buffer;

    public leds(){
        ledStrip = new AddressableLED(port);
        buffer = new AddressableLEDBuffer(length);
        ledStrip.setLength(length);
        ledStrip.start();
    }

    public void makeledscolor(int r, int g, int b){
    for(int i = 1; i<length; i++){
        buffer.setRGB(i, 255, 0, 0);
    }
    ledStrip.setData(buffer);
    }
}
