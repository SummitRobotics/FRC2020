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
import frc.robot.oi.HoodIndicatorWidget;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Homeable;
import frc.robot.lists.Ports;

/**
 * Add your docs here.
 */
public class Hood extends SubsystemBase implements Homeable{

    private CANSparkMax adjustableHood;
    private CANEncoder hoodEncoder;
    private HoodIndicatorWidget indicator;

    //WRONG ANGLE
    private double limeLightMountAngle = 10;
    private double LidarMountAngle = limeLightMountAngle;
    //this is in cm
    private double TargetHeight = 250 - (76.2/4);

    public Hood(HoodIndicatorWidget indicator){

        this.indicator = indicator;

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

    public double getAngle(){
        double angle = getEncoder()*360;
        //tempary, NOT RIGHT, gear ratio
        angle = angle/40;
        angle = angle/3;
        return angle;
    }
    

    /**
     * compinsated the lidar distance for the lidar mount angle
     * @param reportedDistance the distance reported by the lidar
     * @return the new corrected distance
     */
    public double getCompinsatedLidarDistance(double reportedDistance){
        return reportedDistance*Math.cos(LidarMountAngle);
    }

    /**
     * gets a distance estmate of the target using the limelight
     * @param ReportedAngle the angle from 0 the limelight reports
     * @return the distance estmate
     */
    public double getLimelightDistanceEstmate(double reportedAngle){
        return (reportedAngle+limeLightMountAngle)/Math.tan(reportedAngle);
    }

    //this is WRONG and needs to be made REAL with desmos or somthing (its probably quardatic but who knows)
    public double getHoodAngleFromDistance(double distance){
        double out = distance/8;
        //we dont want to try to move the turret pased the limits
        return Functions.clampDouble(out, 40, 0);
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

     @Override
     public void periodic() {
         indicator.SetValue(getAngle());
     }



}
