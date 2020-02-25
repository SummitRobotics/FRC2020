/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.LoggerAxis;
import frc.robot.subsystems.ClimberArms;
import frc.robot.utilities.Functions;
import frc.robot.utilities.RollingAverage;

public class TrimArms extends CommandBase {

    private LoggerAxis leftSlider;
    private LoggerAxis rightSlider;
    private double startingLeftEncoder;
    private double startingRightEncoder;
    private ClimberArms climber;
    private double leftSlidOffset;
    private double rightSlideOffset;
    private RollingAverage leftAvg = new RollingAverage(8);
    private RollingAverage rightAvg = new RollingAverage(8);

    public TrimArms(LoggerAxis left, LoggerAxis right, ClimberArms climb) {
        leftSlider = left;
        rightSlider = right;
        climber = climb;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        startingLeftEncoder = climber.getLeftEncoderPosition();
        startingRightEncoder = climber.getRightEncoderPosition();
        leftSlidOffset = leftSlider.get();
        rightSlideOffset = rightSlider.get();
        climber.extendClimb();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // gets avgraged slider values
        leftAvg.update(leftSlider.get() - leftSlidOffset);
        double left = leftAvg.getAverage();
        rightAvg.update(rightSlider.get() - rightSlideOffset);
        double right = leftAvg.getAverage();

        // converts slider values to climb encoder values
        double leftEncoderTarget = (350 * left) + startingLeftEncoder;
        double rightEncoderTarget = (350 * right) + startingRightEncoder;

        // moves left arm
        if (!Functions.isWithin(leftEncoderTarget, climber.getLeftEncoderPosition(), 10)) {
            if (leftEncoderTarget > climber.getLeftEncoderPosition()) {
                climber.setLeftMotorPower(.5);
            } else {
                climber.setLeftMotorPower(-.5);
            }
        } else {
            climber.setLeftMotorPower(0);
        }

        // moves right arm
        if (!Functions.isWithin(rightEncoderTarget, climber.getRightEncoderPosition(), 10)) {
            if (rightEncoderTarget > climber.getLeftEncoderPosition()) {
                climber.setLeftMotorPower(.5);
            } else {
                climber.setLeftMotorPower(-.5);
            }
        } else {
            climber.setLeftMotorPower(0);
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        climber.setPower(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
