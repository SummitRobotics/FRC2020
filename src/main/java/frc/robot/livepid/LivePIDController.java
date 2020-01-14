package frc.robot.livepid;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public class LivePIDController extends PIDController {

    String name;
    double defaultP, defaultI, defaultD;
    NetworkTableEntry P, I, D;

    public LivePIDController(String name, double defaultP, double defaultI, double defaultD) {
        super(
            defaultP, 
            defaultI, 
            defaultD
        );

        this.name = name;

        ShuffleboardLayout layout = Shuffleboard.getTab("PID Tuning").getLayout(name);
        P = layout.add("P", defaultP).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
        I = layout.add("I", defaultI).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
        D = layout.add("D", defaultD).withWidget(BuiltInWidgets.kNumberSlider).getEntry();

        this.defaultP = defaultP;
        this.defaultI = defaultI;
        this.defaultD = defaultD;
    }

    public void update() {
        setPID(
            P.getDouble(defaultP), 
            I.getDouble(defaultI), 
            D.getDouble(defaultD)
        );
    }
}