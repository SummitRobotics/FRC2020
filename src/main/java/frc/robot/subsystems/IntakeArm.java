/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;

/**
 * Subsystem to control the intake and its arm
 */
public class IntakeArm extends SubsystemBase implements Logger {

	public boolean isUp;

	private VictorSPX 
	intake,
	pivot;

	private DigitalInput upperLimit;

	// data for logger
	private double 
	pivotPower = 0, 
	intakePower = 0;

	public IntakeArm() {
		intake = new VictorSPX(Ports.INTAKE_ARM_INTAKE);
		pivot = new VictorSPX(Ports.INTAKE_ARM_PIVOT);

		isUp = true;

		upperLimit = new DigitalInput(Ports.UPPER_LIMIT);
	}

	/**
	 * Sets the power of the intake arm motor
	 * @param power the new power for the motor
	 */
	public void setPivotPower(double power) {
		power = Functions.clampDouble(power, 1, -1);
		pivotPower = power;
		pivot.set(ControlMode.PercentOutput, power);
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
	 * Stops the intake arm and intake roller motors
	 */
	public void stop() {
		intake.set(ControlMode.PercentOutput, 0);
		pivot.set(ControlMode.PercentOutput, 0);
	}

	public boolean getUpperLimit() {
		return !upperLimit.get();
	}

	public void coast() {
		pivot.setNeutralMode(NeutralMode.Coast);
	}

	public void brake() {
		pivot.setNeutralMode(NeutralMode.Brake);
	}

	@Override
	public double[] getValues(double[] values) {
		values[LoggerRelations.INTAKE_ARM_INTAKE_POWER.value] = intakePower;
		values[LoggerRelations.INTAKE_ARM_PIVOT_POWER.value] = pivotPower;
		return values;
	}
}
