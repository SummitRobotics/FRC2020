// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utilities;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import frc.robot.subsystems.Drivetrain;

/** Add your docs here. */
public class SerialisableMultiGearTrajectory implements Serializable {

    //random number
    private static final long serialVersionUID = 256594604239963776L;

    private Trajectorist highGear;

    private Trajectorist LowGear;

    private static double accelration = 2.5;

    private static double velocity = 2.5;

    private static TrajectoryConfig lowConfig = new TrajectoryConfig(velocity, accelration)
    // Add kinematics to ensure max speed is actually obeyed
    .setKinematics(Drivetrain.DriveKinimatics)
    // Apply the voltage constraint
    .addConstraint(Drivetrain.LowVoltageConstraint);

    private static TrajectoryConfig highConfig = new TrajectoryConfig(velocity, accelration)
    // Add kinematics to ensure max speed is actually obeyed
    .setKinematics(Drivetrain.DriveKinimatics)
    // Apply the voltage constraint
    .addConstraint(Drivetrain.LowVoltageConstraint);

    /**
     * makes a new object
     * @param high the high gear trejectory
     * @param low the low gear trejectory
     */
    public SerialisableMultiGearTrajectory(Trajectorist high, Trajectorist low){
        this.highGear = high;
        this.LowGear = low;
    }

    public Trajectory getHighTrejectory(){
        return highGear;
    }

    public Trajectory getLowTrajectory(){
        return LowGear;
    }

    public Trajectory getTrajectory(boolean shift){
        if(shift){
            return highGear;
        }
        else{
            return LowGear;
        }
    }

    /**
     * creats a new SerialisableMultiGearTrejectory from a list of points
     * @param stat the stating position of the robot (normaly 0,0)
     * @param points the points to travle through
     * @param end the point for the robot to end at
     * @return the new object ready to save
     */
    public static SerialisableMultiGearTrajectory createSerialisableMultiGearTrejectory(Pose2d stat, List<Translation2d> points, Pose2d end){

        return new SerialisableMultiGearTrajectory(
            (Trajectorist)TrajectoryGenerator.generateTrajectory(stat, points, end, highConfig), 
            (Trajectorist)TrajectoryGenerator.generateTrajectory(stat, points, end, lowConfig)
            );
    }

    /**
     * creats a new SerialisableMultiGearTrejectory from a pathwever jason file
     * @param jasonPath path to jason file
     * @return the object ready to save
     * warning CAN FAIL AND RETURN AN EMPTY TRAJECTORY
     */
    public static SerialisableMultiGearTrajectory createSerialisableMultiGearTrejectory(String jasonPath){
        Trajectory trajectory = new Trajectory();
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(jasonPath);
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + jasonPath, ex.getStackTrace());
            throw(new RuntimeException("reading trejectory jason failed"));
        }

        return new SerialisableMultiGearTrajectory(
            (Trajectorist)trajectory,
            (Trajectorist)trajectory
            );
    }

}
