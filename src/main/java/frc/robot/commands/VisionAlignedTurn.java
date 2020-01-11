package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.devices.Lemonlight;
import frc.robot.subsystems.Drivetrain;

public class VisionAlignedTurn extends PIDCommand {

    private static final double
    P = 0,
    I = 0,
    D = 0;

    public VisionAlignedTurn(Drivetrain drivetrain, Lemonlight limelight) {
        super(
            new PIDController(P, I, D),
            limelight::getHorizontalOffset,
            0,
            drivetrain::turn,
            drivetrain
        );
    }
}