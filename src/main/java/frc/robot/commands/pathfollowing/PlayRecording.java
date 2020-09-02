package frc.robot.commands.pathfollowing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class PlayRecording extends CommandBase {

    private class Point {
        public double left, right;

        public Point(String[] data) {
            left = Double.parseDouble(data[0]);
            right = Double.parseDouble(data[1]);
        }
    }

    private File recording;
    private Drivetrain drivetrain;

    private ArrayList<Point> points;
    private int currentStep;

    private boolean aborted = false;

    public PlayRecording(String recording, Drivetrain drivetrain) {
        this.recording = new File("/home/admin/recordings/saved_recordings/" + recording);
        this.drivetrain = drivetrain;
    }

    public PlayRecording(Drivetrain drivetrain) {
        this.recording = new File("/home/admin/recordings/cache");
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        try (Scanner reader = new Scanner(recording)) {
            String fileID = reader.nextLine();
            System.out.println("Running recording: " + fileID);

            while (reader.hasNextLine()) {
                String rawCode = reader.nextLine();

                if (rawCode == "STOP") {
                    break;
                }

                points.add(new Point(rawCode.split(", ")));
            }

        } catch (IOException x) {
            aborted = true;
            System.out.println("Target file could not be accessed, aborting...");
        }

        drivetrain.zeroEncoders();
    }

    @Override
    public void execute() {
        
    }
}