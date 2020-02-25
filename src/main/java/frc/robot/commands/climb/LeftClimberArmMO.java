package frc.robot.commands.climb;

import frc.robot.oi.LoggerAxis;
import frc.robot.subsystems.ClimberArms;
import frc.robot.utilities.MOCommand;

public class LeftClimberArmMO extends MOCommand {

    private ClimberArms climber;
    private LoggerAxis controlAxis;

    public LeftClimberArmMO(ClimberArms climber, LoggerAxis controlAxis) {
        super();

        this.climber = climber;
        this.controlAxis = controlAxis;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setLeftMotorPower(0);
    }

    @Override
    public void execute() {
        climber.setLeftMotorPower(controlAxis.get());
    }

    @Override
    public void end(boolean interrupted) {
        climber.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}