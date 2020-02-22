/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shifter;
import frc.robot.oi.LoggerAxis;

public class ArcadeDrive extends CommandBase {

    private Drivetrain drivetrain;
    private Shifter shift;

    private LoggerAxis forwardPowerAxis;
    private LoggerAxis reversePowerAxis;
    private LoggerAxis turnAxis;

    private final double deadzone = .1;
    
    /**
     * teleop driver control
     * @param drivetrain drivetrain instance
     * @param shift shifter instance
     * @param powerAxis control axis for the drivetrain power
     * @param turnAxis control axis for the drivetrain turn
     */
    public ArcadeDrive(
        Drivetrain drivetrain, 
        Shifter shift, 
        LoggerAxis forwardPowerAxis, 
        LoggerAxis reversePowerAxis, 
        LoggerAxis turnAxis)
    {

        this.drivetrain = drivetrain;
        this.shift = shift;

        this.forwardPowerAxis = forwardPowerAxis;
        this.reversePowerAxis = reversePowerAxis;
        this.turnAxis = turnAxis;

        addRequirements(drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        drivetrain.setOpenRampRate(0);
        shift.lowGear();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        double power = forwardPowerAxis.get() - reversePowerAxis.get();

        if (Math.abs(power) < deadzone) {
            power = 0;
        }

        double turn = turnAxis.get();

        // turn deadzone
        if (Math.abs(turn) < deadzone) {
            turn = 0;
        }

        // power rate of change
        /*
        if (power > old + max_change_rate) {
            power = old + max_change_rate;
            old = power;
        } else if (power < old - max_change_rate) {
            power = old - max_change_rate;
            old = power;
        } else {
            old = power;
        }
        */

        // calculates power to the motors
        double leftPower = power + turn;
        double rightPower = power - turn;

        drivetrain.setLeftMotorPower(leftPower);
        drivetrain.setRightMotorPower(rightPower);

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(final boolean interrupted) {
        drivetrain.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
