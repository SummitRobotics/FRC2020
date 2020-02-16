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
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.utilities.Ports;

//TODO - gut entire subsystem

/**
 * Subsystem to control the internal conveyor mechanism of the robot
 */
public class Conveyor extends SubsystemBase implements Logger {

	public enum States {
		SHOOT,
		SAFE_SHOOT,
		INTAKE,
		OFF
	}

	//TODO - make good
	public static final double
	SHOOT_POWER = 1,
	SUBSUME_POWER = 0.75;

	// powers saved for faster logging
	private double power;

	// conveyor motors
	private VictorSPX conveyorMotor;

	// breakbeam sensors
	private DigitalInput breakbeamEnter;
	private DigitalInput breakbeamExit;

	private boolean 
	safeShootMode,
	shootMode,
	intakeMode;

	public Command
	toggleSafeShootMode,
	toggleShootMode,
	toggleIntakeMode;
	

	public Conveyor() {
		conveyorMotor = new VictorSPX(Ports.CONVEYOR.port);

		breakbeamEnter = new DigitalInput(Ports.BREAKBEAM_ENTER.port);
		breakbeamExit = new DigitalInput(Ports.BREAKBEAM_EXIT.port);

		power = 0;

		// Internal commands for toggling shooter flags
		toggleSafeShootMode = new StartEndCommand(
			this::enableSafeShootMode, 
			this::disableSafeShootMode
		);

		toggleShootMode = new StartEndCommand(
			this::enableShootMode, 
			this::disableShootMode
		);

		toggleIntakeMode = new StartEndCommand(
			this::enableIntakeMode, 
			this::disableIntakeMode
		);
	}

	/**
	 * Sets the power of the conveyor
	 * 
	 * @param power the new power
	 */
	public void setConveyor(double power) {
		conveyorMotor.set(ControlMode.PercentOutput, power);
	}

	//TODO - Make sure that the docs are correct on the nature of breakbeam returns
	/**
	 * Gets the state of the entry breakbeam sensor
	 * 
	 * @return the breakbeam state where true is unbroken and false is broken
	 */
	public boolean getBreakBeamEnter() {
		return breakbeamEnter.get();
	}

	/**
	 * Gets the state of the entry breakbeam sensor
	 * 
	 * @return the breakbeam state where true is unbroken and false is broken
	 */
	public boolean getBreakBeamExit() {
		return breakbeamExit.get();
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
	 * @return the acntive state
	 */
	public States getState() {
		if (shootMode) {
			return States.SAFE_SHOOT;
		}

		if (safeShootMode) {
			return States.SHOOT;
		}

		if (intakeMode) {
			return States.INTAKE;
		}

		return States.OFF;
	}

	/**
	 * Sets the safe shoot mode flag to true
	 */
	public void enableSafeShootMode() {
		safeShootMode = true;
	}

	/**
	 * Sets the safe shoot mode flag to false
	 */
	public void disableSafeShootMode() {
		safeShootMode = false;
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

	@Override
	public double[] getValues(double[] values) {
		values[LoggerRelations.CONVEYOR.value] = power;
		values[LoggerRelations.CONVEYOR_BREAKBEAM_ENTER.value] = getBreakBeamEnter() ? 1 : 0;
		values[LoggerRelations.CONVEYOR_BREAKBEAM_EXIT.value] = getBreakBeamExit() ? 1 : 0;
		return values;
	}
}
