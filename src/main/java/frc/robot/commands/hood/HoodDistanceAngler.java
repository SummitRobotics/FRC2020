/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hood;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commandegment.CommandBase;
import frc.robot.devices.LidarLight;
import frc.robot.subsystems.Hood;
import frc.robot.utilities.Functions;

/**
 * THIS SHOULD ONLY BE USED IN A SHOOTING ASSEMBLY, IT SHOULD NOT BE USED BY
 * ITSELF
 */
public class HoodDistanceAngler extends CommandBase {

    private Hood hood;
    private PIDController pid;
    private boolean lessThanTransfer = false;

    private LidarLight lidarlight;

    public HoodDistanceAngler(Hood hood, LidarLight lidarLight) {
        addRequirements(hood);

        this.hood = hood;
        this.lidarlight = lidarLight;

        // FINE probably dont touch
        pid = new PIDController(0.02, 0.015, 0);
        pid.setTolerance(2, 1);
        pid.setSetpoint(0);
        SmartDashboard.putData("hood", pid);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double angle = hood.getAngle();
        // SmartDashboard.putNumber("Hood Angle Current", angle);
    
        double setpoint = getAngleFromDistance(lidarlight.getBestDistance());

        // SmartDashboard.putNumber("Hood Angle Setpoint", setpoint);
        pid.setSetpoint(setpoint);

        double power = pid.calculate(angle);
        hood.setPower(power);
    }

    public double getAngleFromDistance(double distance) {
        double out = 0;

        double target = 155;
        double offset = 15;

        if (lessThanTransfer && distance > target + offset) {
            lessThanTransfer = false;
        } else if (!lessThanTransfer && distance <= target - offset) {
            lessThanTransfer = true;
        }

        if (lessThanTransfer && distance < target + offset) {
            out = (0.063 * distance) + 14.4861;
        } else {
            out = 31.5;
        }

        // if (distance > 155) {
        //     out = 31.5;
        // } else {
        //     out = (0.0677965 * distance) + 14.4861;
        // }

        return Functions.clampDouble(out, 32, 0);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        pid.reset();
        pid.close();

        hood.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    /**
     * gets if the hood is at the target angle to shoot from the distance
     * 
     * @return true means it is ready to shoot
     */
    public boolean isAtTargetAngle() {
        return Math.abs(pid.getPositionError()) < 2;
    }
}
