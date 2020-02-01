package frc.robot.utilities;

import java.util.Arrays;

public class RollingAverage {

    private Double[] rollingAverageArray;
    private int rollingTarget;

    public RollingAverage(int size) {
        rollingAverageArray = new Double[size];
    }

    public void update(double value) {
        if (rollingAverageArray[rollingTarget] == null) {
            Arrays.fill(rollingAverageArray, value);
        } else {
            rollingAverageArray[rollingTarget] = value;
        }

        rollingTarget = (rollingTarget + 1) % rollingAverageArray.length;
    }

    public double getAverage() {
        double value = 0;
        for (Double d : rollingAverageArray) {
            value += d;
        }

        value /= rollingAverageArray.length;
        return value;
    }
}