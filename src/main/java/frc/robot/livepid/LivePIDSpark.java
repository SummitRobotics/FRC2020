package frc.robot.livepid;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public class LivePIDSpark {

	private static HashMap<String, NetworkTableEntry[]> PIDNetworkEntries = new HashMap<>();

    private double defaultP, defaultI, defaultD;
	private NetworkTableEntry[] PID;
	
	CANPIDController pidController;

	private double
	currentP,
	currentI,
	currentD;

	public LivePIDSpark(String name, CANPIDController pidController, double defaultP, double defaultI, double defaultD, double scaleP, double scaleI, double scaleD) {
		this.pidController = pidController;

		ShuffleboardLayout layout = Shuffleboard.getTab("PID Tuning").getLayout(name, BuiltInLayouts.kList);
        if (PIDNetworkEntries.containsKey(name)) {
            PID = PIDNetworkEntries.get(name);

        } else {
            PID = new NetworkTableEntry[3];

            PID[0] = layout.add("P", defaultP).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", scaleP)).getEntry();
            PID[1] = layout.add("I", defaultI).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", scaleI)).getEntry();
            PID[2] = layout.add("D", defaultD).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", scaleD)).getEntry();
        }

        this.defaultP = defaultP;
        this.defaultI = defaultI;
		this.defaultD = defaultD;
		
		currentP = getP();
		currentI = getI();
		currentD = getD();
	}

	public void update() {
		double p = getP();
		double i = getI();
		double d = getD();

		if (p != currentP || i != currentI || d != currentD) {
			pidController.setP(p);
			pidController.setI(i);
			pidController.setD(d);

			currentP = p;
			currentI = i;
			currentD = d;
		}
	}

	private double getP() {
		return PID[0].getDouble(defaultP);
	}

	private double getI() {
		return PID[1].getDouble(defaultI);
	}

	private double getD() {
		return PID[2].getDouble(defaultD);
	}
}