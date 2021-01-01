/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Driver for virtual shuffleboard buttons
 */
public class shufHELLboardDriver {

    private NetworkTable InfoTable = NetworkTableInstance.getDefault().getTable("RobotInfo");
    private NetworkTable ButtonTable = NetworkTableInstance.getDefault().getTable("Buttons");

    // Variables refering to shuffleboard need to be static for reasons
    public ShuffleboardLEDButton
    recordStart = new ShuffleboardLEDButton(ButtonTable.getEntry("record Start")),
    intake = new ShuffleboardLEDButton(ButtonTable.getEntry("record Intake")),
    shoot = new ShuffleboardLEDButton(ButtonTable.getEntry("record Shoot")),
    shift = new ShuffleboardLEDButton(ButtonTable.getEntry("record Shift")),
    finish = new ShuffleboardLEDButton(ButtonTable.getEntry("record Stop")),

    homeTurret = new ShuffleboardLEDButton(ButtonTable.getEntry("Home Turret")),
    homeHood = new ShuffleboardLEDButton(ButtonTable.getEntry("Home Hood"));

    public HoodIndicatorWidget hoodIndicator = new HoodIndicatorWidget(InfoTable.getEntry("hood"));
    public TurretIndicatorWidget turretIndicator = new TurretIndicatorWidget(InfoTable.getEntry("turret"));

    public StatusDisplay statusDisplay = new StatusDisplay(InfoTable.getEntry("status"));

    public NetworkTableEntry shooterSpeed = InfoTable.getEntry("Shooter Speed");
    public NetworkTableEntry shooterTemp = InfoTable.getEntry("Shooter Temp");

    public NetworkTableEntry pressure = InfoTable.getEntry("pressure");
}
