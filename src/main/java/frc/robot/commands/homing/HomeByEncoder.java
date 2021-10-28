// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.homing;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utilities.Homeable;

public class HomeByEncoder extends CommandBase {

    private boolean setlimits;
    private Homeable toHome;
    private double homingPower;
    private int minLoops;
    private double reverseLimit;
    private double fowardLimit;
    private double timeout;

    private Timer timeoutTimer;

    private int loops;

    public HomeByEncoder(Homeable toHome, double homingPower, int minLoops, double timeout) {
        this.toHome = toHome;
        this.homingPower = homingPower;
        this.minLoops = minLoops;
        this.timeout = timeout;

        loops = 0;

        setlimits = false;
        timeoutTimer = new Timer();

        addRequirements(toHome.getSubsystemObject());
    }

    public HomeByEncoder(Homeable toHome, double homingPower, int minLoops, double reverseLimit, double fowardLimit, double timeout) {
        this.toHome = toHome;
        this.homingPower = homingPower;
        this.minLoops = minLoops;
        this.reverseLimit = reverseLimit;
        this.fowardLimit = fowardLimit;
        this.timeout = timeout;

        setlimits = true;

        loops = 0;
        timeoutTimer = new Timer();

        addRequirements(toHome.getSubsystemObject());
    }

    public HomeByEncoder(Homeable toHome, double homingPower, int minLoops, double reverseLimit, double fowardLimit, double timeout, boolean aaaa) {
        this.toHome = toHome;
        this.homingPower = homingPower;
        this.minLoops = minLoops;
        this.reverseLimit = reverseLimit;
        this.fowardLimit = fowardLimit;
        this.timeout = timeout;

        setlimits = true;

        loops = 0;
        timeoutTimer = new Timer();

        //addRequirements(toHome.getSubsystemObject());
    }

    public HomeByEncoder getDuplicate() {
        if (setlimits) {
            return new HomeByEncoder(toHome, homingPower, minLoops, reverseLimit, fowardLimit, timeout);
        }
        return new HomeByEncoder(toHome, homingPower, minLoops, timeout);
    }

    public HomeByEncoder getDuplicate(boolean requireSubsystems) {
        if (setlimits) {
            if (requireSubsystems) {
                return new HomeByEncoder(toHome, homingPower, minLoops, reverseLimit, fowardLimit, timeout, true);
            }

            return new HomeByEncoder(toHome, homingPower, minLoops, reverseLimit, fowardLimit, timeout);
        }
        return new HomeByEncoder(toHome, homingPower, minLoops, timeout);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        loops = 0;
        timeoutTimer.reset();
        timeoutTimer.start();
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
        boolean timeExpired = timeoutTimer.get() > timeout;
        
        if (done) {
            System.out.println("homing of " + 
            toHome.getSubsystemObject().getClass().getCanonicalName() + 
            " is done with " + loops + " loops and a velocity of " + v + " rpm" );
        }
        if(timeExpired){
            System.out.println("homing of " + toHome.getSubsystemObject().getClass().getCanonicalName() + " timed out");
        }
        return done || timeExpired;
    }
}
