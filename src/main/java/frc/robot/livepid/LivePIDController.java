package frc.robot.livepid;

import java.util.HashMap;
import java.util.Map;

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


    /**
     * loive pid controler
     * @param name name to display
     * @param defaultP
     * @param defaultI
     * @param defaultD
     */
    public LivePIDController(String name, double defaultP, double defaultI, double defaultD, double scaleP, double scaleI, double scaleD) {
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

            PID[0] = layout.add("P", defaultP).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", scaleP)).getEntry();
            PID[1] = layout.add("I", defaultI).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", scaleI)).getEntry();
            PID[2] = layout.add("D", defaultD).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", scaleD)).getEntry();

            PIDNetworkEntries.put(name, PID);
        }

        this.defaultP = defaultP;
        this.defaultI = defaultI;
        this.defaultD = defaultD;
    }

    public void update() {
        double p = PID[0].getDouble(defaultP);
        double i = PID[1].getDouble(defaultI);
        double d = PID[2].getDouble(defaultD);
        System.out.println(p);
        setPID(
            p,i,d
        );
    }
}