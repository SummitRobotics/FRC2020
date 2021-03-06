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
public class SerialisableMultiGearTrejectory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Trajectory highGear;

    private Trajectory LowGear;

    private double accelration = 2.5;

    private double velocity = 2.5;

    private static TrajectoryConfig lowConfig = new TrajectoryConfig(2.5, 2.5)
    // Add kinematics to ensure max speed is actually obeyed
    .setKinematics(Drivetrain.DriveKinimatics)
    // Apply the voltage constraint
    .addConstraint(Drivetrain.LowVoltageConstraint);

    private static TrajectoryConfig highConfig = new TrajectoryConfig(2.5, 2.5)
    // Add kinematics to ensure max speed is actually obeyed
    .setKinematics(Drivetrain.DriveKinimatics)
    // Apply the voltage constraint
    .addConstraint(Drivetrain.LowVoltageConstraint);

    /**
     * makes a new object
     * @param high the high gear trejectory
     * @param low the low gear trejectory
     */
    public SerialisableMultiGearTrejectory(Trajectory high, Trajectory low){
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

    public static SerialisableMultiGearTrejectory createSerialisableMultiGearTrejectory(Pose2d stat, List<Translation2d> points, Pose2d end){

        return new SerialisableMultiGearTrejectory(
            TrajectoryGenerator.generateTrajectory(stat, points, end, highConfig), 
            TrajectoryGenerator.generateTrajectory(stat, points, end, lowConfig)
            );
    }

    public static SerialisableMultiGearTrejectory createSerialisableMultiGearTrejectory(String jasonPath){
        Trajectory trajectory = new Trajectory();
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(jasonPath);
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + jasonPath, ex.getStackTrace());
        }

        return new SerialisableMultiGearTrejectory(
            trajectory,
            trajectory
            );
    }

}
