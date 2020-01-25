package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;


/**
 * Subsystem to control the turret
 */
public class Turret extends SubsystemBase implements Logger {

    double oldPower = 0;

    CANSparkMax turret;
    CANEncoder encoder;
    CANPIDController pidController;

    //TODO - Fix PID and FF values
    private static final double
    P = 0,
    I = 0,
    D = 0,
    FF = 0;

    public Turret() {
        turret = new CANSparkMax(Ports.TURRET.port, MotorType.kBrushless);
        encoder = turret.getEncoder();
        pidController = turret.getPIDController();

        turret.setClosedLoopRampRate(0);

        pidController.setP(P);
        pidController.setI(I);
        pidController.setD(D);
        pidController.setFF(FF);
        pidController.setOutputRange(-1, 1);

        turret.setInverted(true);
    }

    /**
     * Sets power of the turret motor
     * 
     * @param power new power for the motor
     */
    public void setPower(double power){
        oldPower = power;
        power = Functions.clampDouble(power, 1, -1);
        turret.set(power);
    }

    /**
     * Sets encoder target for turret
     * 
     * @param value new setpoint for the motor
     */
    public void setTarget(double value) {
        pidController.setReference(value, ControlType.kPosition);
    }

    /**
     * Stops the turret motor
     */
    public void stop() {
        turret.set(0);
    }

    /**
     * Logs the power of the turret
     */
    @Override
    public double[] getValues(double[] values) {
        values[LoggerRelations.TURRET.value] = oldPower;
        return values;
    }
}