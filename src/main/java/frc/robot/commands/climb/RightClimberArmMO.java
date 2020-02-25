package frc.robot.commands.climb;

import frc.robot.oi.LoggerAxis;
import frc.robot.subsystems.ClimberArms;
import frc.robot.utilities.MOCommand;

public class RightClimberArmMO extends MOCommand {

    private ClimberArms climber;
    private LoggerAxis controlAxis;

    public RightClimberArmMO(ClimberArms climber, LoggerAxis controlAxis) {
        super();

        this.climber = climber;
        this.controlAxis = controlAxis;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setRightMotorPower(0);
    }

    @Override
    public void execute() {
        climber.setRightMotorPower(controlAxis.get());
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