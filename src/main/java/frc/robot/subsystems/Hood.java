/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Homeable;
import frc.robot.utilities.Ports;

/**
 * Add your docs here.
 */
public class Hood extends SubsystemBase implements Homeable{

    private CANSparkMax adjustableHood;
    private CANEncoder hoodEncoder;

    public Hood(){

        adjustableHood = new CANSparkMax(Ports.ADJUSTABLE_HOOD, MotorType.kBrushless);

        hoodEncoder = adjustableHood.getEncoder();
        hoodEncoder.setPosition(0);

        adjustableHood.setSmartCurrentLimit(30);

        adjustableHood.disableVoltageCompensation();

        adjustableHood.setIdleMode(IdleMode.kBrake);

    }

        /**
     * Stops the motor
     */
    public void stop() {
        adjustableHood.set(0);
    }

    public void setPower(double power) {
        power = Functions.clampDouble(power, 1, -1);
        adjustableHood.set(power);
    }

    public double getEncoder(){
        return hoodEncoder.getPosition();
    }

     //for homing adj hood
     @Override
     public double getCurrent() {
         return adjustableHood.getOutputCurrent();
     }
 
     @Override
     public double getVelocity() {
         return hoodEncoder.getVelocity();
     }
 
     @Override
     public void setHomingPower(double power) {
         setPower(power);
 
     }
 
     @Override
     public void setHome(double position) {
         hoodEncoder.setPosition(position);
     }
 
     @Override
     public void setSoftLimits(double revers, double fowards) {
         //enables both soft limits
         adjustableHood.setSoftLimit(SoftLimitDirection.kForward, (float)fowards);
         adjustableHood.setSoftLimit(SoftLimitDirection.kReverse, (float)revers);
 
     }
 
     @Override
     public void DisableSoftLimits() {
         adjustableHood.enableSoftLimit(SoftLimitDirection.kForward, false);
         adjustableHood.enableSoftLimit(SoftLimitDirection.kReverse, false);
 
     }
 
     @Override
     public void EnableSoftLimits() {
         adjustableHood.enableSoftLimit(SoftLimitDirection.kForward, true);
         adjustableHood.enableSoftLimit(SoftLimitDirection.kReverse, true);
 
     }
 
     @Override
     public Subsystem getSubsystemObject(){
         return this;
     }



}
