/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.networktables.EntryNotification;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.utilities.functionalinterfaces.MidiAction;

/**
 * Add your docs here.
 */
public class Midi {

    private NetworkTable ccTable;
    private NetworkTable noteTable;
    private NetworkTable Return;

    private Timer t;

    public Midi(){
        t = new Timer();
        t.start();
        NetworkTableInstance netTableInstance = NetworkTableInstance.getDefault();
        ccTable = netTableInstance.getTable("ccTable");
        noteTable = netTableInstance.getTable("noteTable");
        Return = netTableInstance.getTable("Return");
        netTableInstance.setUpdateRate(0.01);
    }

    /**
     * sends a cc to the contoler
     * @param id id of the cc to send
     * @param value the cc value
     */
    public void sendCC(int id, int value){
        NetworkTableEntry x = Return.getEntry(Integer.toString(id));
        x.setNumber(value);
    }

    /**
     * gets a cc value
     * @param id the id of the cc you want to get
     * @return the value or 0 if nothing is curently stored
     */
    public int getCC(int id){
        NetworkTableEntry x = ccTable.getEntry(Integer.toString(id));
        return (int)x.getNumber(0);
    }

    /**
     * gets a note state
     * @param id the id of the note you want to get
     * @return true of false, deafult to false if onthing is stored
     */
    public boolean getNote(int id){
        NetworkTableEntry x = noteTable.getEntry(Integer.toString(id));
        return x.getBoolean(false);
    }

    /**
     * sets the action to occure on a spisfic cc changing
     * @param action the midiaction to occure
     * @param id the id of the cc to act upon
     */
    public void setCcAction(MidiAction action, int id){
        NetworkTableEntry x = ccTable.getEntry(Integer.toString(id));
        x.addListener((EntryNotification n) -> {action.act(n);}, 20);
    }

    /**
     * sets the action to occure on a spisfic note changing
     * @param action the midiaction to occure
     * @param id the id of the note to act upon
     */
    public void setNoteAction(MidiAction action, int id){
        NetworkTableEntry x = noteTable.getEntry(Integer.toString(id));
        x.addListener((EntryNotification n) -> {action.act(n);}, 20);
    }

}
