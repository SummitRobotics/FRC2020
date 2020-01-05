package frc.robot.logging;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Constants.LoggerRelations;

/**
 * Class to manage robot logging. It acts as a psuedosubsystem in order to use
 * the scheduler's periodic method to make updates.
 */
public class SyncLogger implements Subsystem {

    private ArrayList<Logger> elements;
    private int attempts, logNumber;
    private String logFileLocation;
    private double[] values;

    /**
     * @param elements all logger classes
     */
    public SyncLogger() {
        this.elements = new ArrayList<>();
        values = new double[LoggerRelations.values().length];

        generateLogFile();
    }

    public void addElements(Logger... newElements) {
        Collections.addAll(elements, newElements);
    }

    /**
     * Is run every loop. Will log data based on the rate value
     */
    @Override
    public void periodic() {
        if (attempts == 0) {
            writeLogFile();
        }

        attempts = (attempts++) % Constants.LOGGER_RATE;
    }

    /**
     * Creates a new log file
     */
    private void generateLogFile() {
        attempts = 0;
        values = new double[LoggerRelations.values().length];

        logNumber = getLogNumber() + 1;
        setLogNumber(logNumber);

        logFileLocation = Constants.LOG_FILE_PATH + "SyncLog-" + logNumber + ".csv";
    }

    /**
     * Reads the LFN file to find the current log number
     * @return the log number, or 0 if no LFN file is found
     */
    private int getLogNumber() {
        try (FileReader reader = new FileReader(Constants.LOG_FILE_PATH + "LFN.txt")) {
            return reader.read();

        } catch (IOException x) {
            System.out.println("LFN file not found");
            return 0;
        }
    }

    /**
     * Sets the log number in the LFN
     * @param num value to set the LFN to
     */
    private void setLogNumber(int num) {
        try (FileWriter writer = new FileWriter(Constants.LOG_FILE_PATH + "LFN.txt")) {
            writer.write(num);
        } catch (IOException x) {
            System.out.println("LFN file not found");
        }
    }

    /**
     * Writes a new entry in an open log file
     */
    private void writeLogFile() {
        try (FileWriter writer = new FileWriter(logFileLocation, true)) {
            writer.append(System.nanoTime() / 1_000_000_000 + ", ");
            writer.append(getFormatedLogData());
            writer.append("\n");

        } catch (IOException x) {
            System.out.println("Could not find or create log file at location: " + logFileLocation);
        }
    }

    /**
     * Retrieves log data from logging classes 
     */
    private void getLogData() {
        for (Logger l : elements) {
            values = l.getValues(values);
        }
    }

    /**
     * Formats log data into a comma seperated string
     * @return formated data
     */
    private String getFormatedLogData() {
        getLogData();
        String data = Arrays.toString(values);
        return data.substring(1, data.length()-2);
    }
}