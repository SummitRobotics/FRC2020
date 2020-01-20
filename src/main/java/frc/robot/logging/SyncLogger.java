package frc.robot.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Constants.LoggerRelations;

/**
 * Class to manage robot logging. It acts as a psuedosubsystem in order to use
 * the scheduler's periodic method to make updates.
 */
public class SyncLogger implements Subsystem, Command {

    private ArrayList<Logger> elements;
    private int attempts;
    private String logFileLocation;
    private double[] values;

    private Timer robotTimer = new Timer();

    private SimpleDateFormat timeStampFormatter;
    private SimpleDateFormat fileFormatter;

    private HashSet<Subsystem> requirements;

    /**
     * @param elements all logger classes
     */
    public SyncLogger() {
        this.elements = new ArrayList<>();
        values = new double[LoggerRelations.values().length];

        timeStampFormatter = new SimpleDateFormat("HH:mm:ss");
        fileFormatter = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");

        requirements = new HashSet<>();
        requirements.add(this);
    }

    /**
     * adds passed in instances to logger so data from it is logged the passed in
     * must implement logger
     * 
     * @param newElements the instances to add to the logger
     */
    public void addElements(Logger... newElements) {
        // adds passed in instace to logger elements array
        Collections.addAll(elements, newElements);
    }

    /**
     * Creates a new log file
     */
    @Override
    public void initialize() {
        // timer stuff
        robotTimer.reset();
        robotTimer.start();

        attempts = 0;
        values = new double[LoggerRelations.values().length];

        Date timeInMillis = new Date(System.currentTimeMillis());
        String logTimeStamp = fileFormatter.format(timeInMillis);

        logFileLocation = Constants.LOG_FILE_PATH + "SyncLog-" + logTimeStamp + ".csv";
    }

    /**
     * Is run every loop. Will log data based on the rate value
     */
    @Override
    public void execute() {
        if (attempts == 0) {
            writeLogFile();
        }

        attempts = (attempts++) % Constants.LOGGER_RATE;
    }

    /**
     * Writes a new entry in an open log file
     */
    private void writeLogFile() {
        getFormatedLogData();
        try (FileWriter writer = new FileWriter(logFileLocation, true)) {
            writer.append(timeStampFormatter.format(System.currentTimeMillis()) + ", ");
            writer.append(Math.round(robotTimer.get() * 1000) + ", ");
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
        for (Logger loggerImplementation : elements) {
            values = loggerImplementation.getValues(values);
        }
    }

    /**
     * Formats log data into a comma seperated string
     * 
     * @return formated data
     */
    private String getFormatedLogData() {
        getLogData();
        String data = Arrays.toString(values);
        return data.substring(1, data.length() - 1);
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return requirements;
    }
}