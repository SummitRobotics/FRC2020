package frc.robot.utilities.powerlimiting;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.devices.PDP;
import frc.robot.utilities.Functions;
import frc.robot.utilities.RollingAverage;

public class PowerLimiter implements Subsystem, Command{

    private PDP pdp;
    private LimitedSubsystem[] subsystems;

    private PIDController pidController;

    private HashSet<Subsystem> requirements;

    private final double VOLTAGE_TARGET = 9.5;

    private RollingAverage average = new RollingAverage(32);

    public PowerLimiter(PDP pdp, LimitedSubsystem... subsystems) {
        this.pdp = pdp;
        this.subsystems = subsystems;
        
        this.pidController = new PIDController(1, 0, 0);

        this.pidController.setSetpoint(VOLTAGE_TARGET);

        requirements = new HashSet<>();
        requirements.add(this);
    }

    @Override
    public void execute() {
        //gets the lowest of the last 4 voltages from the pdp
        double voltage = pdp.getMinimumPDPVoltage();

        //does the pid calculations and limits the output to 0-100
        double pidValue = Functions.clampDouble(pidController.calculate(voltage), 100, 0);

        //puts the value into a rolling average
        average.update(pidValue);

        //loops through all subsystems and applys new value
        for (LimitedSubsystem s : subsystems) {
            s.limitPower(average.getAverage() * s.getPriority());
        }
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return requirements;
    }
}
