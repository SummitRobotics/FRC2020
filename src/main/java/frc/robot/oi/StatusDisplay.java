/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import java.util.HashMap;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.lists.Colors;

/**
 * Add your docs here.
 */
public class StatusDisplay extends SubsystemBase{

    private NetworkTableEntry entry;

    private String message;

    private Color8Bit deafultColor;

    private HashMap<String, StatusMessage> NameAndMessage;

    private boolean changed;
    

    public StatusDisplay(NetworkTableEntry entry){
        this.entry = entry;
        changed = true;
        NameAndMessage = new  HashMap<>();
        deafultColor = Colors.White;
        entry.forceSetString("");
    }

    /**
     * @param color the color to use when none is provided, deafults to white
     */
    public void SetDeafultColor(Color8Bit color){
        deafultColor = color;
    }

    /**
     * adds a status to display
     * @param Status the message to display
     * @param color the color8bit to display it in
     */
    public void addStatus(String name, String Status, Color8Bit color, int prioirty){
        changed = true;
        NameAndMessage.put(name, new StatusMessage(prioirty, Status+":"+hexStringFomColor(color)));
    }

    /**
     * adds a status to display
     * @param Status the message to display
     * @param color the css compatable color string to dsiplay it in
     * for example #ff0000 or red are both valid and would be the same
     */
    public void addStatus(String name, String Status, String color, int prioirty){
        changed = true;
        NameAndMessage.put(name, new StatusMessage(prioirty, Status+":"+color));
    }

    /**
     * adds a status to display
     * @param Status the message to display
     * the takes no color and insted uses the color set with {@link #SetDeafultColor(Color8Bit color)} 
     */
    public void addStatus(String name, String Status, int prioirty){
        changed = true;
        NameAndMessage.put(name, new StatusMessage(prioirty, Status+":"+hexStringFomColor(deafultColor)));
    }

    /**
     * @return the curent status
     */
    public String getStatus(){
        return message;
    }

    public void removeStatus(String name){
        changed = true;
        NameAndMessage.remove(name);
    }

    private String hexStringFomColor(Color8Bit color){
        String out = "#";
        String[] cs = new String[3];
        cs[0] = Integer.toHexString(color.red);
        cs[1] = Integer.toHexString(color.green);
        cs[2] = Integer.toHexString(color.blue);
        for(String x : cs){
            if(x.length() > 2){
                x = x.substring(x.length()-3, x.length()-1);
            }
            if (x.length() < 2){
                x = "0" + x;
            }
            out = out + x;
        }
        return out;
    }

       //sorts and displays the messages
       @Override
       public void periodic() {
           if(changed){
               //sorts through all messages and gets the one with the highest priority
                int CurrentPrioirty = Integer.MIN_VALUE;
                String message = "";
                for (StatusMessage x : NameAndMessage.values()){
                    if(x.getPrioirty() > CurrentPrioirty){
                        CurrentPrioirty = x.getPrioirty();
                        message = x.getMessage();
                    }
                }
                //displays the message
                entry.setString(message);

                changed = false;  
           }
       }

    private class StatusMessage{

        private int prioirty;
        private String message;

        public StatusMessage(int prioirty, String message){
            this.prioirty = prioirty;
            this.message = message;
        }

        public String getMessage(){
            return message;
        }

        public int getPrioirty(){
            return prioirty;
        }
    }

}
