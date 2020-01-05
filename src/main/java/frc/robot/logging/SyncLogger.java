package frc.robot.logging;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Constants.LoggerRelations;

public class SyncLogger implements Subsystem{

    private Logger elements[];
    private int attempts, logNumber;
    private String logFileLocation;
    private double[] values;

    public SyncLogger(Logger[] elements) {
        this.elements = elements;
        values = new double[LoggerRelations.values().length];

        generateLogFile();
    }

    @Override
    public void periodic() {
        if (attempts == 0) {
            writeLogFile();
        }

        attempts = (attempts++) % Constants.LOGGER_RATE;
    }

    private void generateLogFile() {
        attempts = 0;
        values = new double[LoggerRelations.values().length];

        logNumber = getLogNumber() + 1;
        setLogNumber(logNumber);

        logFileLocation = Constants.LOG_FILE_PATH + "SyncLog-" + logNumber + ".csv";
    }

    private int getLogNumber() {
        try (FileReader reader = new FileReader(Constants.LOG_FILE_PATH + "LFN.txt")) {
            return reader.read();

        } catch (IOException x) {
            System.out.println("LFN file not found");
            return 0;
        }
    }

    private void setLogNumber(int num) {
        try (FileWriter writer = new FileWriter(Constants.LOG_FILE_PATH + "LFN.txt")) {
            writer.write(num);
        } catch (IOException x) {
            System.out.println("LFN file not found");
        }
    }

    private void writeLogFile() {
        try (FileWriter writer = new FileWriter(logFileLocation, true)) {
            writer.append(System.nanoTime() / 1_000_000_000 + ", ");
            writer.append(getFormatedLogData());
            writer.append("\n");

        } catch (IOException x) {
            System.out.println("Could not find or create log file at location: " + logFileLocation);
        }
    }

    private void getLogData() {
        for (Logger l : elements) {
            values = l.getValues(values);
        }
    }

    private String getFormatedLogData() {
        getLogData();
        String data = Arrays.toString(values);
        return data.substring(1, data.length()-2);
    }
}