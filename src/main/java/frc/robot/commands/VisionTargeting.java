package frc.robot.commands;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.sensors.Lemonlight;
import frc.robot.subsystems.Drivetrain;

public class VisionTargeting extends PIDCommand{

    Lemonlight limelight;
    Drivetrain drivetrain;

    public VisionTargeting(Lemonlight limelight, Drivetrain drivetrain) {
        /*
        this.limelight = limelight;
        this.drivetrain = drivetrain;

        super(
            //TODO - Tune PID values
            new PIDController(0, 0, 0),
            limelight::getHorizontalOffset,
            0,
            output -> 
        )
        */
    }
    
}