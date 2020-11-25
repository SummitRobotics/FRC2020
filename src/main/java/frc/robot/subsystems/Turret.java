package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;


/**
 * Subsystem to control the turret
 */
public class Turret extends SubsystemBase {

    private CANSparkMax turret;
    private CANEncoder encoder;
    private CANPIDController pidController;

    private DigitalInput limit;

    public Turret() {
        turret = new CANSparkMax(Ports.TURRET, MotorType.kBrushless);
        //encoder = turret.getEncoder();
        //pidController = turret.getPIDController();

        //turret.setClosedLoopRampRate(0);
        //pidController.setOutputRange(-1, 1);

        turret.setInverted(false);
        turret.setIdleMode(IdleMode.kCoast);
    }

    /**
     * Sets the encoder position to 0
     */
    public void resetEncoder() {
        encoder.setPosition(0);
    }

    /**
     * Sets the soft limits based on limit switch
     */
    public void calibrateEncoder() {
        resetEncoder();

        //TODO - Make sure directions are correct and make values correct based on gear ratios
        turret.setSoftLimit(SoftLimitDirection.kForward, 0);
        turret.setSoftLimit(SoftLimitDirection.kReverse, 0);
    }

    /**
     * Gets the value of limit switch one
     * 
     * @return whether the button is pressed
     */
    public boolean getLimit() {
        return limit.get();
    }

    /**
     * Sets power of the turret motor
     * 
     * @param power new power for the motor
     */
    public void setPower(double power){
        System.out.println(power);
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
}