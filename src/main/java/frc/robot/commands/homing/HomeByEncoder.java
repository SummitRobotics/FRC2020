// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.homing;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.commandegment.CommandBase;
import frc.robot.utilities.Homeable;
import frc.robot.utilities.lists.commandPriorities;

public class HomeByEncoder extends CommandBase {

    private boolean setlimits;
    private Homeable toHome;
    private double homingPower;
    private int minLoops;
    private double reverseLimit;
    private double fowardLimit;

    private int loops;

    public HomeByEncoder(Homeable toHome, double homingPower, int minLoops) {
        this.toHome = toHome;
        this.homingPower = homingPower;
        this.minLoops = minLoops;

        loops = 0;

        setlimits = false;

        addRequirements(toHome.getSubsystemObject());
        setPriority(commandPriorities.HomingPriority);
    }

    public HomeByEncoder(Homeable toHome, double homingPower, int minLoops, double reverseLimit, double fowardLimit) {
        this.toHome = toHome;
        this.homingPower = homingPower;
        this.minLoops = minLoops;
        this.reverseLimit = reverseLimit;
        this.fowardLimit = fowardLimit;

        setlimits = true;

        loops = 0;

        addRequirements(toHome.getSubsystemObject());
    }

    public HomeByEncoder getDuplicate() {
        if (setlimits) {
            return new HomeByEncoder(toHome, homingPower, minLoops, reverseLimit, fowardLimit);
        }
        return new HomeByEncoder(toHome, homingPower, minLoops);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        loops = 0;
        toHome.DisableSoftLimits();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        toHome.setHomingPower(homingPower);
        loops += (loops > minLoops)? 0 : 1;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        toHome.setHomingPower(0);
        if (!interrupted) {
            toHome.setHome(0);
            if (setlimits) {
                toHome.setSoftLimits(reverseLimit, fowardLimit);
                toHome.EnableSoftLimits();
            }
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        double v = toHome.getVelocity();

        boolean done = (loops > minLoops) && (Math.abs(v) < 1);
        
        return done;
    }
}
