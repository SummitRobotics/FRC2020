package frc.robot.commands.pathfollowing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import frc.robot.commandegment.CommandBase;
import frc.robot.commandegment.CommandSchedulest;
import frc.robot.commandegment.InstantCommand;
import frc.robot.commands.drivetrain.EncoderDrive;
import frc.robot.commands.intake.SetDown;
import frc.robot.commands.intake.SetLoad;
import frc.robot.commands.intake.SetUp;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.Shifter;

public class PlayRecording extends CommandBase {
//TODO make this also a subsystem too so that we can use subsystem periodic() 
//insted of execute to remove the 20ms lag betwen a command being scheduled and 
//being run the first time beacuse the scheduler is dumb and owen won't let me change it
//DONT REMOVE COMMENT UNTIL DONE

    private File recording;

    private Drivetrain drivetrain;
    private Shifter shifter;
    private IntakeArm intake;

    private ArrayList<String> points = new ArrayList<>();
    private int currentStep = 0;

    //the end function must have been lost in a merge or somthing as this should end i think
    private boolean aborted = false;

    private CommandSchedulest scheduler;

    private EncoderDrive CurrentDrivetrainCommand;

    private String recordingName;


    public PlayRecording(CommandSchedulest scheduler, String recording, Drivetrain drivetrain, Shifter shift, IntakeArm intake) {
        this.scheduler = scheduler;
        this.recording = new File("/home/admin/recordings/saved/" + recording);
        this.drivetrain = drivetrain;
        this.shifter = shift;
        this.intake = intake;

        recordingName = recording;
    }

    public PlayRecording(CommandSchedulest scheduler, Drivetrain drivetrain) {
        this.scheduler = scheduler;
        this.recording = new File("/home/admin/recordings/cache.chs");
        this.drivetrain = drivetrain;

        recordingName = "cache";
    }

    @Override
    public void initialize() {
        try (Scanner reader = new Scanner(recording)) {
            String creationTime = reader.nextLine();
            System.out.println("Running recording: " + recordingName + " created at " + creationTime);

            while (reader.hasNextLine()) {
                String rawLine = reader.nextLine();

                if(rawLine.contains("sequence: end")){
                    break;
                }
                
                points.add(rawLine);

            }

        } catch (IOException x) {
            aborted = true;
            System.out.println("Target file could not be accessed, aborting...");
        }

        //drivetrain.zeroEncoders();
    }

    @Override
    public void execute() {
        if(!checkForRunningCommands()){
            parseNewCommand();
            currentStep++;
            if(currentStep > (points.size()-1)){
                aborted = true;
            }
        }
        
    }

    private void parseNewCommand(){

        System.out.println(currentStep);
        String command = points.get(currentStep);


        if(command.contains("drivetrain: ")){
            try{
                String[] values = command.split(" ")[1].split(",");
                double left = Double.parseDouble(values[0]);
                double right = Double.parseDouble(values[1]);
                CurrentDrivetrainCommand = new EncoderDrive(drivetrain, left, right);
                scheduler.schedule(CurrentDrivetrainCommand);
                System.out.println("running drivetrain to " + left + ", " + right);
            }
            catch(Exception e){
                System.out.println("error parsing '" + command +"'");
            }
        }
        else if(command.contains("shift: ")){
            if(command.contains("shift: high")){
                scheduler.schedule(new InstantCommand(shifter::highGear));
            }
            else if(command.contains("shift: low")){
                scheduler.schedule(new InstantCommand(shifter::lowGear));
            }
            else{
                System.out.println("error parsing '" + command +"'");
            }
        }
        else if(command.contains("intake: ")){
            if(command.contains("intake: " + IntakeArm.States.UP.toString())){
                scheduler.schedule(new SetUp(intake));
            }
            else if(command.contains("intake: " + IntakeArm.States.DOWN_YES_INTAKE.toString())){
                scheduler.schedule(new SetDown(intake));
            }
            else if(command.contains("intake: " + IntakeArm.States.DOWN_NO_INTAKE.toString())){
                scheduler.schedule(new SetLoad(intake));
            }
            else{
                System.out.println("error parsing '" + command +"'");
            }

        }
        else{
            System.out.println("error parsing '" + command +"'");
        }
    }

    private boolean checkForRunningCommands(){
        if(CurrentDrivetrainCommand == null){
            return false;
        }
        else{
            return scheduler.isScheduled(CurrentDrivetrainCommand);
        }   
    }

    //TODO why does this not end???

}