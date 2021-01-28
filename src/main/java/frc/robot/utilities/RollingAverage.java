package frc.robot.utilities;

import java.util.Arrays;

/**
 * Gets a running average of a value
 */
public class RollingAverage {

    private Double[] rollingAverageArray;
    private int rollingTarget;
    private boolean initialized;
    private boolean fill;

    /**
     * creates new rollingavrage
     * @param size the number of values you want to avrage
     */
    public RollingAverage(int size, boolean fill) {
        rollingAverageArray = new Double[size];

        initialized = false;
        this.fill = fill;
    }

    /**
     * Adds a new value to the average
     * 
     * @param value the new value
     */
    public void update(double value) {
        if (!initialized) {
            if (fill) {
                Arrays.fill(rollingAverageArray, value);
            } else {
                Arrays.fill(rollingAverageArray, 0);
            }
        } else {
            rollingAverageArray[rollingTarget] = value;
        }

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