package frc.robot.utilities.powerlimiting;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.devices.PDP;
import frc.robot.utilities.Functions;

public class PowerLimiter implements Subsystem, Command{

    private PDP pdp;
    private LimitedSubsystem[] subsystems;

    private PIDController pidController;

    private HashSet<Subsystem> requirements;

    private final double VOLTAGE_TARGET = 9.5;

    public PowerLimiter(PDP pdp, LimitedSubsystem... subsystems) {
        this.pdp = pdp;
        this.subsystems = subsystems;
        
        this.pidController = new PIDController(0.075, 0.001, 0);

        this.pidController.setSetpoint(VOLTAGE_TARGET);



        requirements = new HashSet<>();
        requirements.add(this);
    }

    @Override
    public void execute() {
        double voltage = pdp.getMinimumPDPVoltage();

        double pidValue = Functions.clampDouble(pidController.calculate(voltage), 100, 0);
        
        //loops through all subsystems and applys new value
        for (LimitedSubsystem s : subsystems) {
            s.limitPower(pidValue * s.getPriority());
        }
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return requirements;
    }
}
