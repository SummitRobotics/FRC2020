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

public class HoodToAngle extends CommandBase {
    private Hood hood;
    private PIDController pid;

    private double angle;

    public HoodToAngle(Hood hood, double angle) {
        addRequirements(hood);
        this.hood = hood;
        this.angle = angle;
        pid = new PIDController(0.01, 0, 0);
        pid.setTolerance(0.1, 1);
        pid.setSetpoint(0);
        // SmartDashboard.putData("hood to angle nv", pid);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        pid.setSetpoint(angle);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        hood.setPower(pid.calculate(hood.getAngle()));
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
        // return pid.atSetpoint();
        return false;
    }

}
