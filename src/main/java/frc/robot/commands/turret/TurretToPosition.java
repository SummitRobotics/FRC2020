/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Turret;

public class TurretToPosition extends CommandBase {

    private Turret turret;
    private double target;

    private PIDController pid;

    /**
     * Creates a new TurretToPosition.
     */
    public TurretToPosition(Turret turret, double targetAngle) {
        addRequirements(turret);
        this.turret = turret;
        target = targetAngle;
        // GOOD DONT CHANGE
        pid = new PIDController(0.024, 0.0, 0.0009);
        pid.setTolerance(.75, 1);
        SmartDashboard.putData("turret angle pid", pid);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        pid.setSetpoint(target);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        turret.setPower(pid.calculate(turret.getAngle()));
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        pid.reset();
        pid.close();

        turret.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return pid.atSetpoint();
        // return false;
    }
}
