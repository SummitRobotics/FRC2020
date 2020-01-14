package frc.robot.livepid;

import java.util.HashMap;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public class LivePIDController extends PIDController {

    private static HashMap<String, NetworkTableEntry[]> PIDNetworkEntries = new HashMap<>();

    private double defaultP, defaultI, defaultD;
    private NetworkTableEntry[] PID;

    public LivePIDController(String name, double defaultP, double defaultI, double defaultD) {
        super(
            defaultP, 
            defaultI,
            defaultD
        );

        ShuffleboardLayout layout = Shuffleboard.getTab("PID Tuning").getLayout(name, BuiltInLayouts.kList);
        if (PIDNetworkEntries.containsKey(name)) {
            PID = PIDNetworkEntries.get(name);

        } else {
            PID = new NetworkTableEntry[3];

            PID[0] = layout.add("P", defaultP).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
            PID[1] = layout.add("I", defaultI).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
            PID[2] = layout.add("D", defaultD).withWidget(BuiltInWidgets.kNumberSlider).getEntry();

            PIDNetworkEntries.put(name, PID);
        }

        this.defaultP = defaultP;
        this.defaultI = defaultI;
        this.defaultD = defaultD;
    }

    public void update() {
        setPID(
            PID[0].getDouble(defaultP), 
            PID[1].getDouble(defaultI), 
            PID[2].getDouble(defaultD)
        );
    }
}