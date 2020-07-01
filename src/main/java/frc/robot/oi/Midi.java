/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.utilities.Functions;

/**
 * Add your docs here.
 */
public class Midi {

    private NetworkTable ccTable;
    private NetworkTable noteTable;
    private NetworkTable CCReturn;
    private NetworkTable NoteReturn;

    public Midi(){
        NetworkTableInstance netTableInstance = NetworkTableInstance.getDefault();
        ccTable = netTableInstance.getTable("ccTable");
        noteTable = netTableInstance.getTable("noteTable");
        CCReturn = netTableInstance.getTable("CCReturn");
        NoteReturn = netTableInstance.getTable("NoteReturn");
        netTableInstance.setUpdateRate(0.01);
    }

    /**
     * sends a raw cc to the contoler
     * @param id id of the cc to send
     * @param value the cc value (0-127)
     */
    public void sendCC(int id, int value){
        value = (int)Functions.clampDouble(value, 127, 0);
        NetworkTableEntry x = CCReturn.getEntry(Integer.toString(id));
        x.setNumber(value);
    }

    /**
     * sends a raw note-on to the contoler
     * @param id id of the note to send
     * @param value the note value (0-127)
     */
    public void sendNote(int id, int value){
        value = (int)Functions.clampDouble(value, 127, 0);
        NetworkTableEntry x = NoteReturn.getEntry(Integer.toString(id));
        x.setNumber(value);
    }

    /**
     * gets the raw value
     * @param id the id of the cc you want to get
     * @param deafult the deafult value if none is present
     * @return 0-127
     */
    public int getCC(int id, int deafult){
        NetworkTableEntry x = ccTable.getEntry(Integer.toString(id));
        return (int)x.getNumber(deafult);
    }

    /**
     * gets a note state
     * @param id the id of the note you want to get
     * @return true of false, deafults to false if nothing is stored
     */
    public boolean getNote(int id){
        NetworkTableEntry x = noteTable.getEntry(Integer.toString(id));
        return x.getBoolean(false);
    }
}
