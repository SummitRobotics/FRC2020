package frc.robot.oi;

import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.functionalinterfaces.AxisGetter;

/**
 * Wrapper for axes that allows them to be both logged and passed as variables
 */
public class LoggerAxis implements Logger {

	private LoggerRelations logReference;
	private AxisGetter getter;

	public LoggerAxis(AxisGetter getter, LoggerRelations logReference) {
		this.getter = getter;
		this.logReference = logReference;
	}

	public LoggerAxis(AxisGetter getter, LoggerRelations logReference, SyncLogger logger) {
		this.getter = getter;
		this.logReference = logReference;

		logger.addElements(this);
	}

	/**
	 * Gets the position of the axis
	 * @return the position
	 */
	public double get() {
		return getter.get();
	}

	@Override
	public double[] getValues(double[] values) {
		values[logReference.value] = getter.get();
		return null;
	}
}