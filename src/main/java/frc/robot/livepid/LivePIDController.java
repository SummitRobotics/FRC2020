package frc.robot.livepid;

import java.io.FileReader;
import java.io.IOException;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.*;
import frc.robot.utilities.Constants;

public class LivePIDController extends PIDController{

    String name;
    NetworkTableEntry P, I, D;

    public LivePIDController(String name, double defaultP, double defaultI, double defaultD) {
        super(
            defaultP,
            defaultI,
            defaultD
        );

        this.name = name;

        try (FileReader reader = new FileReader(Constants.PID_VALUES_PATH)) {

        } catch (IOException x) {
            System.out.println("PID file could not be found or created");
        }
    }

    public void configureShuffleboard() {
        ShuffleboardLayout layout = Shuffleboard.getTab("PID Tuners").getLayout(name, BuiltInLayouts.kList);

        P = layout.add("P", 0).getEntry();
        I = layout.add("I", 0).getEntry();
        D = layout.add("D", 0).getEntry();
    }
}