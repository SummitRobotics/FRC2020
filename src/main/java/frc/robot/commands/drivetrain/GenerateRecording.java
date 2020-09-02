package frc.robot.commands.drivetrain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Drivetrain;

public class GenerateRecording extends CommandBase {

    public static File cacheFile = new File("/home/admin/recordings/cache");

    private Drivetrain drivetrain;

    private LoggerButton savePoint;
    private boolean savePointPrior;
    
    private LoggerButton saveSequence;
    private boolean saveSequencePrior;

    private SimpleDateFormat timeStampFormatter;

    private boolean aborted;

    /**
     * Generates a recording of an autonomous sequence, and stores it to a cache file.
     * 
     * File format is as follows:
     * 1. Timestamp: "HH:mm:ss"
     * 2+. left motor pos, right motor pos, shooter mode (number for setpoint, string for auto WIP)
     * 
     * @param drivetrain drivetrain subsystem
     * @param savePoint button to control save value
     * @param saveSequence button to store sequence
     */
    public GenerateRecording(Drivetrain drivetrain, LoggerButton savePoint, LoggerButton saveSequence) {
        this.drivetrain = drivetrain;
        this.savePoint = savePoint;
        this.saveSequence = saveSequence;

        timeStampFormatter = new SimpleDateFormat("HH:mm:ss");

        aborted = false;

    }

    @Override
    public void initialize() {
        try (FileWriter writer = new FileWriter(cacheFile)) {
            long currentTime = System.currentTimeMillis();
            String formattedTime = timeStampFormatter.format(currentTime);

            writer.append(timeStampFormatter.format(currentTime + "\n"));

            System.out.println("Recording Started at: " + formattedTime);

        } catch(IOException x) {
            aborted = true;

            System.out.println("CacheFile could not be accessed, aborting...");
        }
    }

    @Override
    public void execute() {
        boolean savePointCurrent = savePoint.get();
        boolean saveSequenceCurrent = saveSequence.get();

        if (!saveSequencePrior && savePointCurrent) {
            aborted = true;
            return;
        }

        if (!savePointPrior && savePointCurrent) {
            double left = drivetrain.getLeftEncoderPosition();
            double right = drivetrain.getRightEncoderPosition();

            addPoint(left, right);
            
            System.out.println("Point added with positions");
            System.out.println("Left: " + left);
            System.out.println("Right: " + right);
            System.out.println("at time: " + timeStampFormatter.format(System.currentTimeMillis()));

        }

        savePointPrior = savePointCurrent;
        saveSequencePrior = saveSequenceCurrent;
    }

    @Override
    public boolean isFinished() {
        return aborted;
    }

    private void addPoint(double left, double right) {

    }
}