package frc.robot.oi;

import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.Functions;
import frc.robot.utilities.functionalinterfaces.AxisGetter;

/**
 * Wrapper for axes that allows them to be both logged and passed as variables
 */
public class LoggerAxis implements Logger {

	private final static double DEFAULT_DEADZONE = 0.05;

	private LoggerRelations logReference;
	private AxisGetter getter;
	private double deadzone;

	public LoggerAxis(AxisGetter getter, LoggerRelations logReference) {
		this(getter, logReference, DEFAULT_DEADZONE);
	}

	public LoggerAxis(AxisGetter getter, LoggerRelations logReference, SyncLogger logger) {
		this(getter, logReference, DEFAULT_DEADZONE);

		logger.addElements(this);
	}

	public LoggerAxis(AxisGetter getter, LoggerRelations logReference, double deadzone) {
		this.getter = getter;
		this.logReference = logReference;
		this.deadzone = deadzone;
	}

	public LoggerAxis(AxisGetter getter, LoggerRelations logReference, SyncLogger logger, double deadzone) {
		this(getter, logReference, deadzone);

		logger.addElements(this);
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
	public double[] getValues(double[] values) {
		values[logReference.value] = getter.get();
		return null;
	}
}