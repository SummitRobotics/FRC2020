/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.utilities.Usable;

/**
 * MUST BE STATIC TO WORK RIGHT
 * LIRTALY UST BE STATIC TO WORK 
 * WILL CRASH THE ROBOT ON SECOND INIT IF NOT STATIC
 */
public class shufHELLboardButton extends Button implements Usable{

    private ArrayList<Object> users;

    private NetworkTableEntry button;

    /**
     * THE BUTTON MUST BE STATIC
     * @param x x value for where the button is placed
     * @param y y value for where the button is placed
     * @param name name of the button
     * @param tab the tab the button should be on
     */
    public shufHELLboardButton(int x, int y, String name, String tab) {
        super();

        button = Shuffleboard.getTab(tab).add(name, false).withWidget("Toggle Button").withPosition(x, y).getEntry();

        users = new ArrayList<>();
    }

    /**
     * Gets the button value
     * @return the button value
     */
    @Override
    public boolean get() {
        if(button.getBoolean(false)){
            button.setBoolean(false);
            return true;
        }
        return false;
    }

    @Override
    public void using(Object user) {
        users.add(user);
    }

    @Override
    public void release(Object user) {
        users.remove(user);
    }

    @Override
    public boolean inUse() {
        return !users.isEmpty();
    }

}
