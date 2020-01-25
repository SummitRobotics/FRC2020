package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;

public class Shooter extends SubsystemBase {

    private CANSparkMax shooterMotor;
    private CANEncoder shooterEncoder;

    public Shooter() {
        shooterMotor = new CANSparkMax(Ports.SHOOTER.port, MotorType.kBrushless);
        shooterEncoder = shooterMotor.getEncoder();

        shooterMotor.setClosedLoopRampRate(0);
    }

    public void setPower(double power) {
        power = Functions.clampDouble(power, 1, -1);
        shooterMotor.set(power);
    }

    public void stop() {
        shooterMotor.set(0);
    }

    public double getRPM() {
        return shooterEncoder.getVelocity();
    }
}