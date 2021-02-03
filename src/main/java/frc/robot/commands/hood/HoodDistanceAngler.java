/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hood;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;

/**
 * THIS SHOULD ONLY BE USED IN A SHOOTING ASSEMBLY, IT SHOULD NOT BE USED BY
 * ITSELF
 */
public class HoodDistanceAngler extends CommandBase {

    private Hood hood;
    private PIDController pid;

    private double distance;

    public HoodDistanceAngler(Hood hood) {
        addRequirements(hood);

        this.hood = hood;
        this.distance = 0;

        // TODO - make good
        pid = new PIDController(0.01, 0, 0);
        pid.setTolerance(0.1, 1);
        pid.setSetpoint(0);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double angle = hood.getAngle();
        double setpoint = hood.getHoodAngleFromDistance(distance);
        pid.setSetpoint(setpoint);
        hood.setPower(pid.calculate(angle));
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
     * sets the distance the hood should go to the angle for
     * 
     * @param distance the distance from the hood to the target
     */
    public void setDistance(double distance) {
        this.distance = distance;
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
