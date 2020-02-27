/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class EncoderDrive extends CommandBase {

    private Drivetrain drivetrain;
    private double ticks;

    public EncoderDrive(Drivetrain drivetrain, double rotations) {
        this.drivetrain = drivetrain;
        this.ticks = rotations;

        addRequirements(drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        drivetrain.stop();
        drivetrain.setLeftEncoder(0);
        drivetrain.setRightEncoder(0);
        drivetrain.setLeftMotorTarget(ticks);
        drivetrain.setRightMotorTarget(ticks);
    }
    

    @Override
    public void execute() {
        System.out.println(drivetrain.getLeftEncoderPosition());
    }
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // return Math.abs(drivetrain.getLeftEncoderPosition() - ticks) < 1 &&
        //     Math.abs(drivetrain.getRightEncoderPosition() - ticks) < 1;
        return false;
    }
}
