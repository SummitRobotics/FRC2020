package frc.robot.commands.pathfollowing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.LoggerButton;
import frc.robot.subsystems.Drivetrain;
//import frc.robot.subsystems.IntakeArm;
//import frc.robot.subsystems.Shifter;

public class GenerateRecording extends CommandBase {

    public static File cacheFile = new File("/home/admin/recordings/cache.chs");

    private Drivetrain drivetrain;
    //private Shifter shifter;
    //private IntakeArm intake;

    private LoggerButton savePoint;
    private boolean savePointPrior;
    
    private LoggerButton saveSequence;
    private boolean saveSequencePrior;

    private SimpleDateFormat timeStampFormatter;

    private boolean aborted;

    private FileWriter file;

    private static ShuffleboardTab tab = Shuffleboard.getTab("record");

    private static NetworkTableEntry saveIntake = tab.add("intake", false).withWidget("Toggle Button").withPosition(0, 0).getEntry();
    //private static NetworkTableEntry saveShooterMode = tab.add("shooter", false).withWidget("Toggle Button").withPosition(1, 0).getEntry();
    private static NetworkTableEntry saveShift = tab.add("shifter", false).withWidget("Toggle Button").withPosition(1, 0).getEntry();
    private static NetworkTableEntry recordOutput = tab.add("Record status", "recorder object made").withPosition(2, 0).withSize(2, 1).getEntry();

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
    public GenerateRecording(Drivetrain drivetrain, LoggerButton savePoint, LoggerButton saveSequence){//, Shifter shifter, IntakeArm intake) {
        this.drivetrain = drivetrain;
        this.savePoint = savePoint;
        this.saveSequence = saveSequence;
        //this.intake = intake;
        //this.shifter = shifter;

        timeStampFormatter = new SimpleDateFormat("HH:mm:ss");

        aborted = false;
    }

    @Override
    public void initialize() {
        try {
            file = new FileWriter(cacheFile);
            long currentTime = System.currentTimeMillis();
            String formattedTime = timeStampFormatter.format(currentTime);

            file.append(timeStampFormatter.format(currentTime + "\n"));
            file.append("sequence: start" + "\n");

            recordOutput.setString("Recording Started at: " + formattedTime);

        } catch(IOException x) {
            aborted = true;
            System.out.println("CacheFile could not be created, aborting...");
        }
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
            
            file.flush();
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

            try {
                file.append("sequence: end" + "\n");
                file.flush();
            }catch(IOException e){
                System.out.println("error while writing file after recording end");
            }
            return;
    }

    private void RecordOnShuffleboard() throws IOException{
        if(saveIntake.getBoolean(false)){
            saveIntake.setBoolean(false);
            addIntakePoint();
        }

        if(saveShift.getBoolean(false)){
            saveShift.setBoolean(false);
            addShiftPoint();
        }

        // if(saveShooterMode.getBoolean(false)){
        //     saveShooterMode.setBoolean(false);
        //     addShootPoint();
        //     recordOutput.setString("saved shoot point");
        // }
    }

    private void addShiftPoint() throws IOException{
        String state = "fake";//shifter.getShiftState() ? "high" : "low";
        file.append("shift: " + state + "\n");
        recordOutput.setString("saved shift " + state);
    }

    private void addShootPoint() throws IOException{
        return;
    }

    private void addIntakePoint() throws IOException{
        String state = "fake";//intake.getState().toString();
        file.append("intake: " + state + "\n");
        recordOutput.setString("saved intake " + state);
    }
    
    private void addDrivetrainPoint() throws IOException{
        double left = drivetrain.getLeftEncoderPosition();
        double right = drivetrain.getRightEncoderPosition();

        file.append("drivetrain: " + left + "," + right + "\n");
    }
}