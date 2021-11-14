/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.RollingAverage;
import frc.robot.utilities.lists.Ports;

/**
 * Subsystem to control the internal conveyor mechanism of the robot
 */
public class Conveyor extends SubsystemBase {

	public enum States {
		SHOOT,
		INTAKE,
		OFF
	}

	public static final double
	SHOOT_POWER = 1,
	SUBSUME_POWER = 0.5;

	//the number of balls shot
	private int ballsShot;

	private int ballShotOfset;

	private boolean initlised;

	private boolean previousSwitchState;

	private RollingAverage switchVals;

	private double lastMotorPower;

	// conveyor motor
	private VictorSPX conveyorMotor;

	// breakbeam sensors
	private DigitalInput breakbutton;

	private NetworkTableEntry ballsEntry;

	private boolean 
	shootMode,
	intakeMode;

	public Conveyor(NetworkTableEntry balls) {
		ballsEntry = balls;

		conveyorMotor = new VictorSPX(Ports.CONVEYOR);

		breakbutton = new DigitalInput(Ports.BREAKBEAM);

		shootMode = false;
		intakeMode = false;
		ballsShot = 0;
		ballShotOfset = 0;
        initlised = false;
        previousSwitchState = false;

		switchVals = new RollingAverage(5, true);
		lastMotorPower = 0;
	}

	/**
	 * Sets the power of the conveyor
	 * 
	 * @param power the new power
	 */
	public void setConveyor(double power) {
		conveyorMotor.set(ControlMode.PercentOutput, -power);
		lastMotorPower = power;
	}

	public double getLastMotorPower(){
		return lastMotorPower;
	}

	/**
	 * Gets the state of the entry breakbeam sensor
	 * 
	 * @return the breakbeam state where true is unbroken and false is broken
	 */
	public boolean getBreakButton() {
		return !breakbutton.get();
	}

	/**
	 * Gets the debounced state of the ball detection switch
	 * 
	 * @return the state of the ball dection switch after debouncing
	 */
	public boolean getDebouncedSwitch() {
		switchVals.update(getBreakButton() ? 1 : 0);
		//if the last 3 reads are 1 return true
		return switchVals.getAverage() >= 0.5;
	}

	/**
	 * Stops both conveyor motors
	 */
	public void stop() {
		setConveyor(0);
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

		else if (intakeMode) {
			return States.INTAKE;
		}

		else return States.OFF;
	}

	//TODO needs to be implimented
	/**
	 * shoots one ball
	 */
	public void shootOneBall(){

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

	/**
	 * gets the number of balls shot sense the robot enabled
	 * @return the number of shots
	 */
	public int getAbsoluteShotBals(){
		return ballsShot;
	}

	/**
	 * gets the number of balls shot sense the last call of resetRelitiveBasl()
	 * @returnthe the number of acumilated shots
	 */
	public int getRelitiveBallsShot(){
		return ballsShot-ballShotOfset;
	}

	/**
	 * resets the number of balls shot for getRelitiveBallShots()
	 */
	public void resetRelitiveBals(){
		ballShotOfset = ballsShot;
	}

	/**
	 * resets the total number of balls the robot has shot, only call when robot disables/enables
	 */
	public void resetAbsoluteBallSHots(){
		ballsShot = 0;
		resetRelitiveBals();
	}

	public void uninitBallCount(){
		resetAbsoluteBallSHots();
	}

	@Override
	public void periodic() {
		//we only want to do things if the robot is enabled, otherwise people can move the bals
		if(DriverStation.getInstance().isEnabled()){
			//sets up important vars on forst run
            boolean switchState = getBreakButton();
            //System.out.println(switchState);

			//detects the switch reverting to its origonal pos whis the motor is runing fowards as a shot ball
			if((switchState != previousSwitchState) && (!switchState) && (getLastMotorPower() > 0)){
                ballsShot +=1;
                //System.out.println("balls + 1");
			}

			previousSwitchState = switchState;
		}
		ballsEntry.forceSetNumber(getAbsoluteShotBals());
	}
}
