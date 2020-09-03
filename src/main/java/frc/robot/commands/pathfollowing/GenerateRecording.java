package frc.robot.commands.pathfollowing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private FileWriter file;

    private NetworkTableEntry recordOutput;
    private NetworkTableEntry saveIntake;
    private NetworkTableEntry saveShooterMode;
    private NetworkTableEntry saveShift;

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
        //try {
            //file = new FileWriter(cacheFile);
            long currentTime = System.currentTimeMillis();
            String formattedTime = timeStampFormatter.format(currentTime);

            initShuffleboard();

            //file.append(timeStampFormatter.format(currentTime + "\n"));
            //file.append("sequence: start" + "\n");

            System.out.println("Recording Started at: " + formattedTime);

        //} catch(IOException x) {
            //aborted = true;
            //System.out.println("CacheFile could not be created, aborting...");
        //}
    }

    private void initShuffleboard(){
        //Shuffleboard.update();
        ShuffleboardTab tab = Shuffleboard.getTab("record");
        
        recordOutput = tab.add("Record status", "recording started").getEntry();
        
        saveIntake = tab.add("intake", false).withWidget("Toggle Button").getEntry();
        saveShooterMode = tab.add("ShootMode", false).withWidget("Toggle Button").getEntry();
        saveShift = tab.add("shifter", false).withWidget("Toggle Button").getEntry();

    }

    @Override
    public void execute() {
        Shuffleboard.update();
        try {
            boolean savePointCurrent = savePoint.get();
            boolean saveSequenceCurrent = saveSequence.get();

            if (saveSequencePrior && !saveSequenceCurrent) {
                aborted = true;
                return;
            }

            if (savePointPrior && !savePointCurrent) {
                addDrivetrainPoint();
                recordOutput.setString("saved drivetrain point");
            }

            savePointPrior = savePointCurrent;
            saveSequencePrior = saveSequenceCurrent;

            RecordOnShuffleboard();
            
            //file.flush();
        }catch(IOException e){
            recordOutput.setString("FILE WRIGHT ERROR");
            System.out.println("error while writing file, aborting");
        }
    }

    @Override
    public boolean isFinished() {
        return aborted;
    }

    @Override
    public void end(boolean interrupted) {
        recordOutput.setString("recording stopped");

            // try {
            //     file.append("sequence: end" + "\n");
            //     file.flush();
            // }catch(IOException e){
            //     System.out.println("error while writing file after recording end");
            // }
            return;
    }

    private void RecordOnShuffleboard() throws IOException{
        if(saveIntake.getBoolean(false)){
            saveIntake.setBoolean(false);
            addIntakePoint();
            recordOutput.setString("saved intake point");
        }

        if(saveShift.getBoolean(false)){
            saveShift.setBoolean(false);
            addShiftPoint();
            recordOutput.setString("saved shift point");
        }

        if(saveShooterMode.getBoolean(false)){
            saveShooterMode.setBoolean(false);
            addShootPoint();
            recordOutput.setString("saved shoot point");
        }
    }

    private void addShiftPoint() throws IOException{
        return;
    }

    private void addShootPoint() throws IOException{
        return;
    }

    private void addIntakePoint() throws IOException{
        return;
    }
    
    private void addDrivetrainPoint() throws IOException{
        double left = drivetrain.getLeftEncoderPosition();
        double right = drivetrain.getRightEncoderPosition();

        //file.append("drivetrain: " + left + ", " + right + "\n");
    }
}