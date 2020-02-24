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
    private int ticks;

    public EncoderDrive(Drivetrain drivetrain, int ticks) {
        this.drivetrain = drivetrain;
        this.ticks = ticks;

        addRequirements(drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        drivetrain.stop();
        drivetrain.setLeftMotorPosition(ticks);
        drivetrain.setRightMotorPosition(ticks);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return Math.abs(drivetrain.getLeftEncoderPosition() - ticks) < 10 ||
            Math.abs(drivetrain.getRightEncoderPosition() - ticks) < 10;
    }
}
