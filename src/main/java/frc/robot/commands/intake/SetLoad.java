/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.IntakeArm.States;

public class SetLoad extends CommandBase {

    private final static double targetTime = 0.25;
    protected IntakeArm intake;

    protected Timer timer = new Timer();
    protected boolean end;

    protected double startTime;

    public SetLoad(IntakeArm intake) {
        this.intake = intake;

        end = false;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();

        if (intake.getState() != States.UP) {
            end = true;

        } else {
            intake.setState(States.DOWN_NO_INTAKE);

            intake.setPivotPower(0.2);
            intake.setIntakePower(0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPivotPower(0);

        timer.stop();
        timer.reset();
    }

    @Override
    public boolean isFinished() {
        return end || timer.get() > targetTime;
    }
}
