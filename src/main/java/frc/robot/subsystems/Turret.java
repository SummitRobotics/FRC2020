package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.powerlimiting.LimitedSubsystem;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;

public class Turret implements Logger, LimitedSubsystem{

    TalonSRX turret = new TalonSRX(Ports.TURRET.port);
    double oldPower = 0;

    public Turret(){
        turret.configPeakCurrentLimit(0);
        turret.configPeakCurrentDuration(0);
    }

    public double getPriority(){
        return .8;
    }
    public void limitPower(double amount){
        turret.configContinuousCurrentLimit((int)(500*amount));
    }

    public void setPower(double power){
        oldPower = power;
        power = Functions.clampDouble(power, 1, -1);
        turret.set(ControlMode.PercentOutput, power);
    }

    public void stop(){
        turret.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public double[] getValues(double[] values) {

        values[LoggerRelations.TURRET.value] = oldPower;

        return values;
    }


}