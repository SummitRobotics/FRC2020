package frc.robot.commands.pathfollowing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.drivetrain.EncoderDrive;
// import frc.robot.commands.intake.SetDown;
// import frc.robot.commands.intake.SetLoad;
// import frc.robot.commands.intake.SetUp;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.subsystems.Drivetrain;
// import frc.robot.subsystems.IntakeArm;
// import frc.robot.subsystems.Shifter;

public class PlayRecording extends CommandBase {
//TODO make this also a subsystem too so that we can use subsystem periodic() 
//insted of exicute to remove the 20ms lag betwen a command being scedualed and 
//being run the first time beacuse the scedualr is dumb and owen wolnt let me change it
//DONT REMOVE COMMENT UNTIL DONE

    private File recording;

    private Drivetrain drivetrain;
    // private Shifter shifter;
    // private IntakeArm intake;
    // private LEDRange intakeLeds;

    private ArrayList<String> points;
    private int currentStep = 0;

    private boolean aborted = false;

    private CommandScheduler scheduler;

    private EncoderDrive CurrentDrivetrainCommand;


    public PlayRecording(CommandScheduler scheduler, String recording, Drivetrain drivetrain){//, Shifter shift, IntakeArm intake, LEDRange intakeLeds) {
        this.scheduler = scheduler;
        this.recording = new File("/home/admin/recordings/saved_recordings/" + recording);
        this.drivetrain = drivetrain;
        //this.shifter = shift;
        //this.intake = intake;
        //this.intakeLeds = intakeLeds;
    }

    public PlayRecording(CommandScheduler scheduler, Drivetrain drivetrain) {
        this.scheduler = scheduler;
        this.recording = new File("/home/admin/recordings/cache.chs");
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        try (Scanner reader = new Scanner(recording)) {
            String fileID = reader.nextLine();
            System.out.println("Running recording: " + fileID);

            boolean sequinceSection = false;
            while (reader.hasNextLine()) {
                String rawLine = reader.nextLine();

                if(rawLine.contains("sequence: start")){
                    sequinceSection = true;
                }
                else if(rawLine.contains("sequence: end")){
                    sequinceSection = false;
                }
                else if(sequinceSection){
                    points.add(rawLine);
                }
                else{}

            }

        } catch (IOException x) {
            aborted = true;
            System.out.println("Target file could not be accessed, aborting...");
        }

        drivetrain.zeroEncoders();
    }

    @Override
    public void execute() {
        if(!checkForRunningCommands()){
            parseNewCommand();
            currentStep++;
        }
        
    }

    private void parseNewCommand(){
        String command = points.get(currentStep);


        if(command.contains("drivetrain: ")){
            try{
                String[] values = command.split(" ")[1].split(",");
                double left = Integer.parseInt(values[0]);
                double right = Integer.parseInt(values[1]);
                CurrentDrivetrainCommand = new EncoderDrive(drivetrain, left, right);
                scheduler.schedule(CurrentDrivetrainCommand);
            }
            catch(Exception e){
                System.out.println("error parsing '" + command +"'");
            }
        }
        // else if(command.contains("shift: ")){
        //     if(command.contains("shift: high")){
        //         scheduler.schedule(new InstantCommand(shifter::highGear));
        //     }
        //     else if(command.contains("shift: low")){
        //         scheduler.schedule(new InstantCommand(shifter::lowGear));
        //     }
        //     else{
        //         System.out.println("error parsing '" + command +"'");
        //     }
        // }
        // else if(command.contains("intake: ")){
        //     if(command.contains("intake: " + intake.state.UP.toString())){
        //         scheduler.schedule(new SetUp(intake));
        //     }
        //     else if(command.contains("intake: " + intake.state.DOWN_YES_INTAKE.toString())){
        //         scheduler.schedule(new SetDown(intake, intakeLeds));
        //     }
        //     else if(command.contains("intake: " + intake.state.DOWN_NO_INTAKE.toString())){
        //         scheduler.schedule(new SetLoad(intake));
        //     }
        //     else{
        //         System.out.println("error parsing '" + command +"'");
        //     }

        // }
        else{
            System.out.println("error parsing '" + command +"'");
        }
    }

    private boolean checkForRunningCommands(){
        return scheduler.isScheduled(CurrentDrivetrainCommand);
    }

}