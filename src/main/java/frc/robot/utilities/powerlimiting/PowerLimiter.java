package frc.robot.utilities.powerlimiting;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.devices.PDP;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Functions;

public class PowerLimiter implements Subsystem, Command {

    private PDP pdp;
    private LimitedSubsystem[] subsystems;

    private PIDController pidController;

    private HashSet<Subsystem> requirements;

    public PowerLimiter(PDP pdp, LimitedSubsystem... subsystems) {
        this.pdp = pdp;
        this.subsystems = subsystems;

        this.pidController = new PIDController(Constants.VOLTAGE_P, Constants.VOLTAGE_I, Constants.VOLTAGE_D);

        this.pidController.setSetpoint(Constants.VOLTAGE_TARGET);

        requirements = new HashSet<>();
        requirements.add(this);
    }

    @Override
    public void execute() {
        System.out.println(pdp.getTotalPDPCurrent());
        double voltage = pdp.getPDPVoltage();
        double pidValue = Functions.clampDouble(pidController.calculate(voltage), 1, 0);

        for (LimitedSubsystem s : subsystems) {
            s.limitPower(pidValue * s.getPriority());
        }
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return requirements;
    }
}