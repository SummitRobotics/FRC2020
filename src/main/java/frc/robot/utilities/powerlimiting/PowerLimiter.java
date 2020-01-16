package frc.robot.utilities.powerlimiting;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.devices.PDP;
import frc.robot.utilities.Constants;

public class PowerLimiter implements Subsystem {

    private PDP pdp;
    private LimitedSubsystem[] subsystems;

    private PIDController pidController;

    public PowerLimiter(PDP pdp, LimitedSubsystem...subsystems) {
        this.pdp = pdp;
        this.subsystems = subsystems;

        this.pidController = new PIDController(
            Constants.VOLTAGE_P,
            Constants.VOLTAGE_I,
            Constants.VOLTAGE_D
        );

        this.pidController.setSetpoint(Constants.VOLTAGE_TARGET);
    }

    @Override
    public void periodic() {
        double voltage = pdp.getPDPVoltage();
        double pidValue = pidController.calculate(voltage);

        for (LimitedSubsystem s : subsystems) {
            s.limitPower(pidValue * s.getPriority());
        }
    }
}