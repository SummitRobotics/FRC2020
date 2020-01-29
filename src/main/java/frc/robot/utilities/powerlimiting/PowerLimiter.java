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

    private double[] oldPid = new double[32];
    private int index = 0;

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
        double voltage = pdp.getMinimumPDPVoltage();
        double pidValue = 1;
        if(voltage<Constants.VOLTAGE_TARGET+.5){
            System.out.println(voltage);
            pidValue = Functions.clampDouble(-pidController.calculate(voltage), 1, 0);
        }
        
        oldPid[index] = pidValue;
        index++;
        if(index == oldPid.length){
            index=0;
        }
        double limit = 0;
        for(int i = 0; i<oldPid.length; i++){
            limit = limit+oldPid[i];
        }
        limit = limit / oldPid.length;

        for (LimitedSubsystem s : subsystems) {
            s.limitPower(limit * s.getPriority());
        }
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return requirements;
    }
}
