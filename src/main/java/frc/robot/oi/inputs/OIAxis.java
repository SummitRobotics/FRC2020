package frc.robot.oi.inputs;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import frc.robot.utilities.Functions;

/**
 * Wrapper for axes that allows for better management
 */
public class OIAxis {

	private final static double DEFAULT_DEADZONE = 0.05;

	protected DoubleSupplier getter;
	protected double deadzone;

	private ArrayList<Object> users;

	public OIAxis(DoubleSupplier getter) {
		this(getter, DEFAULT_DEADZONE);
	}

	public OIAxis(DoubleSupplier getter, double deadzone) {
		this.getter = getter;
		this.deadzone = deadzone;

		users = new ArrayList<>();
	}

	/**
	 * Gets the position of the axis
	 * 
	 * @return the position
	 */
	public double get() {
		double position = getter.getAsDouble();

		if (Functions.isWithin(position, 0, deadzone)) {
			return 0;
		}

		return (1 + deadzone) * position - Math.copySign(deadzone, position);
	}

	/**
	 * @return the deadzone
	 */
	public double getDeadzone() {
		return deadzone;
	}

	/**
	 * @param deadzone the deadzone to set
	 */
	public void setDeadzone(double deadzone) {
		this.deadzone = deadzone;
	}

}