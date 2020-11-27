package frc.robot.utilities;

import java.util.Arrays;

/**
 * Gets a running average of a value
 */
public class RollingAverageNoFill {

    private Double[] rollingAverageArray;
    private int rollingTarget;

    /**
     * creates new rollingavrage
     * @param size the number of values you want to avrage
     */
    public RollingAverageNoFill(int size) {
        rollingAverageArray = new Double[size];
        Arrays.fill(rollingAverageArray, 0.0);
    }

    /**
     * Adds a new value to the average
     * 
     * @param value the new value
     */
    public void update(double value) {
        rollingAverageArray[rollingTarget] = value;

        rollingTarget = (rollingTarget + 1) % rollingAverageArray.length;
    }

    /**
     * Gets the running average
     * 
     * @return the average
     */
    public double getAverage() {
        double value = 0;
        for (Double d : rollingAverageArray) {
            value += d;
        }

        value /= rollingAverageArray.length;
        return value;
    }
}