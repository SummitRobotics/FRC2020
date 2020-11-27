/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Ports;

/**
 * Subsystem to control the internal conveyor mechanism of the robot
 */
public class Conveyor extends SubsystemBase {

	public enum States {
		SHOOT,
		INTAKE,
		OFF
	}

	//TODO - make good
	public static final double
	SHOOT_POWER = 1,
	SUBSUME_POWER = 1;

	// conveyor motor
	private VictorSPX conveyorMotor;

	// breakbeam sensors
	private DigitalInput breakbeam;

	private boolean 
	shootMode,
	intakeMode;

	public Conveyor() {
		conveyorMotor = new VictorSPX(Ports.CONVEYOR);

		breakbeam = new DigitalInput(Ports.BREAKBEAM);

		shootMode = false;
		intakeMode = false;
	}

	/**
	 * Sets the power of the conveyor
	 * 
	 * @param power the new power
	 */
	public void setConveyor(double power) {
		conveyorMotor.set(ControlMode.PercentOutput, -power);
	}

	/**
	 * Gets the state of the entry breakbeam sensor
	 * 
	 * @return the breakbeam state where true is unbroken and false is broken
	 */
	public boolean getBreakBeam() {
		return breakbeam.get();
	}

	/**
	 * Stops both conveyor motors
	 */
	public void stop() {
		conveyorMotor.set(ControlMode.PercentOutput, 0);
	}

	/**
	 * Gets the run state of the conveyor based on mode flags
	 * 
	 * @return the active state
	 */
	public States getState() {
		if (shootMode) {
			return States.SHOOT;
		}

		if (intakeMode) {
			return States.INTAKE;
		}

		return States.OFF;
	}

	/**
	 * Sets the shoot mode flag to true
	 */
	public void enableShootMode() {
		shootMode = true;
	}

	/**
	 * Sets the shoot mode flag to false
	 */
	public void disableShootMode() {
		shootMode = false;
	}

	public void toggleShootMode() {
		shootMode = !shootMode;
	}

	public void setShootMode(boolean newMode) {
		shootMode = newMode;
	}

	/**
	 * Sets the intake mode flag to true
	 */
	public void enableIntakeMode() {
		intakeMode = true;
	}

	/**
	 * Sets the intake mode flag to true
	 */
	public void disableIntakeMode() {
		intakeMode = false;
	}

	public void toggleIntakeMode() {
		intakeMode = !intakeMode;
	}

	public void setIntakeMode(boolean newMode) {
		intakeMode = newMode;
	}

	public boolean getIntakeMode() {
		return intakeMode;
	}
}
