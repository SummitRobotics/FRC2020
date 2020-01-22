package frc.robot.logging;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Class to manage robot logging. It acts as a psuedosubsystem in order to use
 * the scheduler's periodic method to make updates.
 */
public class SyncLogger implements Subsystem {

    private static final String LOG_FILE_PATH = "/home/admin/";
    public final static int LOGGER_RATE = 1;

    private ArrayList<Logger> elements;
    private int attempts, logNumber;
    private String logFileLocation;
    private double[] values;
    private Timer robotTimer = new Timer();

    /**
     * @param elements all logger classes
     */
    public SyncLogger() {
        this.elements = new ArrayList<>();
        values = new double[LoggerRelations.values().length];

        generateLogFile();
    }

    /**
     * adds passed in instances to logger so data from it is logged
     * the passed in must implement logger
     * @param newElements the instances to add to the logger
     */
    public void addElements(Logger... newElements) {
        // adds passed in instace to logger elements array
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

        attempts = (attempts++) % LOGGER_RATE;
    }

    /**
     * Creates a new log file
     */
    private void generateLogFile() {
        //timer stuff
        robotTimer.reset();
        robotTimer.start();

        attempts = 0;
        values = new double[LoggerRelations.values().length];

        logNumber = getLogNumber() + 1;
        setLogNumber(logNumber);

        logFileLocation = LOG_FILE_PATH + "SyncLog-" + logNumber + ".csv";
    }

    /**
     * Reads the LFN file to find the current log number
     * @return the log number, or 0 if no LFN file is found
     */
    private int getLogNumber() {
        try (FileReader reader = new FileReader(LOG_FILE_PATH + "LFN.txt")) {
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
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH + "LFN.txt")) {
            writer.write(num);
        } catch (IOException x) {
            System.out.println("LFN file not found");
        }
    }

    /**
     * Writes a new entry in an open log file
     */
    private void writeLogFile() {
        getFormatedLogData();
        try (FileWriter writer = new FileWriter(logFileLocation, true)) {
            writer.append(Math.round(robotTimer.get()*1000) + ", ");
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
        for (Logger loggerImplimentation : elements) {
            values = loggerImplimentation.getValues(values);
        }
    }

    /**
     * Formats log data into a comma seperated string
     * @return formated data
     */
    private String getFormatedLogData() {
        getLogData();
        String data = Arrays.toString(values);
        return data.substring(1, data.length()-1);
    }
}