package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
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

    public void setPower(double power){
        oldPower = power;
        power = Functions.clampDouble(power, 1, -1);
        turret.set(power);
    }

    public void setTarget(double value) {
        pidController.setReference(value, ControlType.kPosition);
    }

    public void stop(){
        turret.set(0);
    }

    @Override
    public double[] getValues(double[] values) {

        values[LoggerRelations.TURRET.value] = oldPower;

        return values;
    }


}