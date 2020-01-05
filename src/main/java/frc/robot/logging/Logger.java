package frc.robot.logging;

/**
 * Interface to give classes logging functionality
 */
public interface Logger {

    /**
     * Gets new logger values for a logger class
     * @param values a pregenerated list of doubles the size of {@link frc.robot.utilities.Constants.LoggerRelations}
     * @return a list of the same length as the input, but with the cooresponding indexes written to
     */
    public double[] getValues(double[] values);
}