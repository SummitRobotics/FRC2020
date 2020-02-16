/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
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

//TODO - gut entire subsystem

/**
 * Subsystem to control the intake and its arm
 */
public class IntakeArm extends SubsystemBase implements Logger {

	// TODO - make PID right
	private static final double 
	defaultP = 0, 
	defaultI = 0, 
	defaultD = 0;

	private VictorSPX intake;

	private CANSparkMax pivot;
	private CANEncoder pivotEncoder;
	private CANPIDController pivotPID;

	// data for logger
	private double 
	pivotPower = 0, 
	intakePower = 0;

	// PID Config
	private static final int
	PID_OUTPUT_MIN = -1, PID_OUTPUT_MAX = 1;

	public IntakeArm() {
		intake = new VictorSPX(Ports.INTAKE_ARM_INTAKE.port);

		pivot = new CANSparkMax(Ports.INTAKE_ARM_PIVOT.port, MotorType.kBrushless);
		pivotEncoder = pivot.getEncoder();
		pivotPID = pivot.getPIDController();

		pivotEncoder.setPosition(0);
		pivotPID.setOutputRange(PID_OUTPUT_MIN, PID_OUTPUT_MAX);
		resetPID();
	}

	/**
	 * Sets the intake arm's PID values
	 * @param P the new P value
	 * @param I the new I value
	 * @param D the new D value
	 */
	public void setPID(double P, double I, double D) {
		pivotPID.setP(P);
		pivotPID.setI(I);
		pivotPID.setD(D);
	}

	/**
	 * Resets the intake arm's PID to a default value
	 */
	public void resetPID() {
		setPID(defaultP, defaultI, defaultD);
	}

	/**
	 * Sets the power of the intake arm motor
	 * @param power the new power for the motor
	 */
	public void setPivotPower(double power) {
		power = Functions.clampDouble(power, 1, -1);
		pivotPower = power;
		pivot.set(power);
	}

	/**
	 * Sets the power of the intake roller motor
	 * @param power the new power for the motor
	 */
	public void setIntakePower(double power) {
		power = Functions.clampDouble(power, 1, -1);
		intakePower = power;
		intake.set(ControlMode.PercentOutput, power);
	}

	/**
	 * Sets the encoder position of the intake arm
	 * @param position the new position for the encoder
	 */
	public void setPivotEndoderPosition(double position) {
		pivotEncoder.setPosition(position);
	}

	/**
	 * Gets the current position of the encoder
	 * @return the encoder position
	 */
	public double getPivotEncoderPosition() {
		return pivotEncoder.getPosition();
	}

	/**
	 * Sets the position of the intake arm based on encoder values
	 * @param position the position for the arm to go to
	 */
	public void setPivotMotorPosition(double position) {
		pivotPID.setReference(position, ControlType.kPosition);
	}

	/**
	 * Stops the intake arm and intake roller motors
	 */
	public void stop() {
		intake.set(ControlMode.PercentOutput, 0);
		pivot.set(0);
	}

	@Override
	public double[] getValues(double[] values) {
		values[LoggerRelations.INTAKE_ARM_INTAKE_POWER.value] = intakePower;
		values[LoggerRelations.INTAKE_ARM_PIVOT_POWER.value] = pivotPower;
		values[LoggerRelations.INTAKE_ARM_PIVOT_POSITION.value] = getPivotEncoderPosition();
		return values;
	}
}
