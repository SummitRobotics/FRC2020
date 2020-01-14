package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.logging.Logger;
import frc.robot.utilities.Constants;
import frc.robot.utilities.Constants.LoggerRelations;
import frc.robot.utilities.Functions;

public class Turret implements Logger{

    TalonSRX turret = new TalonSRX(Constants.TURRET);
    double oldPower = 0;

    public Turret(){
        //limits motor power so we dont break stuff
        turret.configPeakOutputForward(.2);
        turret.configPeakOutputReverse(-.2);
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