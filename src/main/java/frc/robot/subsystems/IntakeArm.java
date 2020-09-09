package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.Ports;

/**
 * Subsystem to control the intake and its arm
 */
public class IntakeArm extends SubsystemBase {

	public enum States{
		UP,
		DOWN_NO_INTAKE,
		DOWN_YES_INTAKE;
	}

	public final static double intakePower = 0.7;

	public States state;

	private VictorSPX 
	intake,
	pivot;

	private Solenoid lock;

	private DigitalInput upperLimit;

	public IntakeArm() {
		intake = new VictorSPX(Ports.INTAKE_ARM_INTAKE);
		pivot = new VictorSPX(Ports.INTAKE_ARM_PIVOT);
		lock = new Solenoid(Ports.PCM_1, Ports.INTAKE_LOCK);

		upperLimit = new DigitalInput(Ports.UPPER_LIMIT);
		
		state = States.UP;
		brake();
	}

	public void setState(States newState){
		state = newState;
	}

	public States getState(){
		return state;
	}

	/**
	 * Sets the power of the intake arm motor
	 * @param power the new power for the motor
	 */
	public void setPivotPower(double power) {
		power = Functions.clampDouble(power, 1, -1);
		pivot.set(ControlMode.PercentOutput, power);
	}

	/**
	 * Sets the power of the intake roller motor
	 * @param power the new power for the motor
	 */
	public void setIntakePower(double power) {
		power = Functions.clampDouble(power, 1, -1);
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

	public boolean isUp() {
		return state == States.UP;
	}

	public boolean isDown() {
		return state == States.DOWN_YES_INTAKE;
	}

	public boolean isLoading() {
		return state == States.DOWN_NO_INTAKE;
	}

	public void setLock(boolean on) {
		lock.set(on);
	}

	public void openLock() {
		setLock(true);
	}

	public void closeLock() {
		setLock(false);
	}
}
