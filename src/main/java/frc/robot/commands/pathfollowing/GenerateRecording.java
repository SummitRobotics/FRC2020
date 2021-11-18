package frc.robot.commands.pathfollowing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.commandegment.CommandBase;
import frc.robot.commandegment.button.Button;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.Shifter;
//import frc.robot.subsystems.IntakeArm;
//import frc.robot.subsystems.Shifter;
import frc.robot.utilities.SimpleButton;

public class GenerateRecording extends CommandBase {

    public static File cacheFile = new File("/home/admin/recordings/cache.chs");

    private Drivetrain drivetrain;
    private Shifter shifter;
    private IntakeArm intake;

    // private Button savePoint;
    // private boolean savePointPrior;

    private SimpleButton savePoint;
    private SimpleButton saveSequence;

    // private Button saveSequence;
    // private boolean saveSequencePrior;

    // private Button saveShift;
    // private Button saveIntake;
    // private SimpleButton shiftButtonFixer;
    // private SimpleButton intakeButtonFixer;

    private SimpleButton saveShift;
    private SimpleButton saveIntake;

    private SimpleDateFormat timeStampFormatter;

    private boolean aborted;

    private FileWriter file;

    private static ShuffleboardTab tab = Shuffleboard.getTab("record");
    private static NetworkTableEntry recordOutput = tab.add("Record status", "recorder object made").withPosition(4, 0)
            .withSize(2, 1).getEntry();

    /**
     * Generates a recording of an autonomous sequence, and stores it to a cache
     * file.
     * 
     * File format is as follows: 1. Timestamp: "HH:mm:ss" 2+. left motor pos, right
     * motor pos, shooter mode (number for setpoint, string for auto WIP)
     * 
     * @param drivetrain   drivetrain subsystem
     * @param savePoint    button to control save value
     * @param saveSequence button to store sequence
     */
    public GenerateRecording(Drivetrain drivetrain, Shifter shifter, IntakeArm intake, Button savePoint, Button saveSequence, Button saveShift, Button saveIntake) {
        
        this.drivetrain = drivetrain;

        this.savePoint = new SimpleButton(savePoint::get);
        this.saveSequence = new SimpleButton(saveSequence::get);

        this.saveShift = new SimpleButton(saveShift::get);
        this.saveIntake = new SimpleButton(saveIntake::get);

        this.saveShift = new SimpleButton(saveShift);
        this.saveIntake = new SimpleButton(saveIntake);

        this.intake = intake;
        this.shifter = shifter;

        timeStampFormatter = new SimpleDateFormat("HH:mm:ss");

        aborted = false;
    }

    @Override
    public void initialize() {
        try {
            file = new FileWriter(cacheFile);
            long currentTime = System.currentTimeMillis();
            String formattedTime = timeStampFormatter.format(currentTime);

            file.append(formattedTime + "\n");
            file.append("sequence: start" + "\n");

            recordOutput.setString("Recording Started at: " + formattedTime);

            drivetrain.zeroEncoders();

        } catch (IOException x) {
            System.out.println(x);
            aborted = true;
            System.out.println("CacheFile could not be created, aborting..." + x);
        }
    }

    @Override
    public void execute() {
        if (aborted == true) {
            return;
        }

        Shuffleboard.update();

        try {
            if (saveSequence.get()) {
                aborted = true;
                return;
            }

            if (savePoint.get()) {
                addDrivetrainPoint();
                recordOutput.setString("Saved drivetrain point");
            }

            RecordOnShuffleboard();
            
            file.flush();

        } catch (IOException e) {
            recordOutput.setString("FILE WRITE ERROR");
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

            } catch (IOException e) {
                System.out.println("error while writing file after recording end");
            }

            return;
    }

    private void RecordOnShuffleboard() throws IOException{
        if (saveIntake.get()) {
            addIntakePoint();
        }

        if (saveShift.get()) {
            addShiftPoint();
        }

        // if(saveShooterMode.getBoolean(false)){
        //     saveShooterMode.setBoolean(false);
        //     addShootPoint();
        //     recordOutput.setString("saved shoot point");
        // }
    }

    private void addShiftPoint() throws IOException {
        String state = shifter.getShiftState() ? "high" : "low";
        file.append("shift: " + state + "\n");
        recordOutput.setString("saved shift " + state);
    }

    private void addShootPoint() throws IOException {
        return;
    }

    private void addIntakePoint() throws IOException {
        String state = intake.getState().toString();
        file.append("intake: " + state + "\n");
        recordOutput.setString("saved intake " + state);
    }
    
    private void addDrivetrainPoint() throws IOException {
        double left = drivetrain.getLeftEncoderPosition();
        double right = drivetrain.getRightEncoderPosition();

        file.append("drivetrain: " + left + "," + right + "\n");
    }
}