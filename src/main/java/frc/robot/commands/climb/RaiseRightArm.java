/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberArms;

public class RaiseRightArm extends CommandBase {

    private ClimberArms arms;
    private int distance;

    public RaiseRightArm(ClimberArms arms, int distance) {
        this.arms = arms;
        this.distance = distance;

        addRequirements(arms);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        arms.setRightMotorPower(1);
        arms.setRightEncoder(0);
    }

    @Override
    public void execute() {
        System.out.println(arms.getRightEncoderPosition());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        arms.setRightMotorPower(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return arms.getRightEncoderPosition() > distance;
    }
}
