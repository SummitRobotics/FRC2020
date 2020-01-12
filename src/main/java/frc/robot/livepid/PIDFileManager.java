package frc.robot.livepid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import frc.robot.utilities.Constants;

public class PIDFileManager {

    private static PIDFileManager instance;
    private List<String> data;

    public static PIDFileManager getInstance() {
        if (instance == null) {
            instance = new PIDFileManager();
        }
        return instance;
    }

    private PIDFileManager() {
        try {
            data = Files.readAllLines(Paths.get(Constants.PID_VALUES_PATH));

        } catch (IOException x) {
            data = new ArrayList<>();
            System.out.println("File could not be found");
        }
    }

    public double[] getPID(String name) {
        double[] pid = new double[3];

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == name) {
                for (int j = 0; j < 3; j++) {
                    pid[j] = Double.parseDouble(data.get(i + j + 1));
                }
                return pid;
            }
        }
        return null;
    }
}