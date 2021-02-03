/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.homing;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utilities.Homeable;
import frc.robot.utilities.RollingAverage;

public class HomeByCurrent extends CommandBase {

    private boolean setlimits;

    private Homeable toHome;
    private double homingPower;
    private double CurrentThreshold;
    private double reverseLimit;
    private double fowardLimit;

    private RollingAverage currentAverage = new RollingAverage(10, false);

    /**
     * Creates a new HomeByCurrent.
     */
    public HomeByCurrent(Homeable toHome, double homingPower, double CurrentThreshold) {
        this.toHome = toHome;
        this.homingPower = homingPower;
        this.CurrentThreshold = CurrentThreshold;

        setlimits = false;

        addRequirements(toHome.getSubsystemObject());
    }

    public HomeByCurrent(
        Homeable toHome, 
        double homingPower, 
        double CurrentThreshold, 
        double reversLimit,
        double fowardLimit
    ) {
        this.toHome = toHome;
        this.homingPower = homingPower;
        this.CurrentThreshold = CurrentThreshold;
        this.reverseLimit = reversLimit;
        this.fowardLimit = fowardLimit;

        setlimits = true;

        addRequirements(toHome.getSubsystemObject());
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        currentAverage.reset();

        // System.out.println("running");
        toHome.DisableSoftLimits();
    }

    // needed beacuse command groups are dumb
    public HomeByCurrent getDuplicate() {
        if (setlimits) {
            return new HomeByCurrent(toHome, homingPower, CurrentThreshold, reverseLimit, fowardLimit);
        } else
            return new HomeByCurrent(toHome, homingPower, CurrentThreshold);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // System.out.println("homing curent: " + toHome.getCurrent());
        toHome.setHomingPower(homingPower);
        currentAverage.update(toHome.getCurrent());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        toHome.setHomingPower(0);
        System.out.println("homing of " + toHome.getSubsystemObject().getClass().getCanonicalName() + " ended with intrupted "+ interrupted);
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
        double current = currentAverage.getAverage();
        boolean done = current >= CurrentThreshold;
        if (done ){
            System.out.println("homing of " + toHome.getSubsystemObject().getClass().getCanonicalName() + " is done");
        }
        return done;
    }
}
