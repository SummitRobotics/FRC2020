package frc.robot.oi;

import java.util.ArrayList;

import frc.robot.utilities.Functions;
import frc.robot.utilities.Usable;
import frc.robot.utilities.functionalinterfaces.AxisGetter;

/**
 * Wrapper for axes that allows for better management
 */
public class OIAxis implements Usable {

	private final static double DEFAULT_DEADZONE = 0.05;

	private AxisGetter getter;
	private double deadzone;

	private ArrayList<Object> users;

	public OIAxis(AxisGetter getter) {
		this(getter, DEFAULT_DEADZONE);
	}

	public OIAxis(AxisGetter getter, double deadzone) {
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
		double position = getter.get();

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

	@Override
	public void using(Object user) {
		users.add(user);
	}

	@Override
	public void release(Object user) {
		users.remove(user);
	}

	@Override
	public boolean inUse() {
		return !users.isEmpty();
	}
}