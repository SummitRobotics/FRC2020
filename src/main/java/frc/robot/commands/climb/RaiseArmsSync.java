package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberArms;

public class RaiseArmsSync extends CommandBase {

    private ClimberArms climber;
    private int distance;

    private boolean leftFlag, rightFlag;

    public RaiseArmsSync(ClimberArms climber, int distance) {
        this.climber = climber;
        this.distance = distance;

        leftFlag = false;
        rightFlag = true;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setLeftEncoder(0);
        climber.setRightEncoder(0);

        climber.setPower(1);
    }

    @Override
    public void execute() {
        if (climber.getLeftEncoderPosition() > distance && !leftFlag) {
            climber.setLeftMotorPower(0);
            leftFlag = true;
        }

        if (climber.getRightEncoderPosition() > distance && !rightFlag) {
            climber.setRightMotorPower(0);
            rightFlag = true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.setLeftMotorPower(0);
        climber.setRightMotorPower(0);
    }

    @Override
    public boolean isFinished() {
        return leftFlag && rightFlag;
    }
}