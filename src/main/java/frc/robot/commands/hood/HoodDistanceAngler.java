/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hood;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
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

    private LidarLight lidarLight;

    public HoodDistanceAngler(Hood hood, LidarLight lidarLight) {
        addRequirements(hood);

        this.hood = hood;
        this.lidarLight = lidarLight;

        // TODO - make good
        pid = new PIDController(0.01, 0, 0);
        pid.setTolerance(0.1, 1);
        pid.setSetpoint(0);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double angle = hood.getAngle();
        double setpoint = getAngleFromDistance(lidarLight.getBestDistance());
        pid.setSetpoint(setpoint);
        hood.setPower(pid.calculate(angle));
    }

    //TODO - get correct max hood angle
    public double getAngleFromDistance(double distance) {
        double out = 0;

        if (distance > 155) {
            out = 40;
        } else {
            out = (0.0677965 * distance) + 14.4861;
        }

        return Functions.clampDouble(out, 40, 0);
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
        return pid.atSetpoint();
    }
}
