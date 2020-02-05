package frc.robot.oi;

import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;

/**
 * Wrapper class for basic joystick functionality
 */
public class JoystickDriver extends GenericDriver implements Logger {

	public LoggerButton
	button1,
	button2,
	button3,
	button4,
	button5;

	public JoystickDriver(SyncLogger logger) {
		button1 = new LoggerButton(getButtonGetter(1), LoggerRelations.PLACEHOLDER, logger);
		button2 = new LoggerButton(getButtonGetter(2), LoggerRelations.PLACEHOLDER, logger);
		button3 = new LoggerButton(getButtonGetter(3), LoggerRelations.PLACEHOLDER, logger);
		button4 = new LoggerButton(getButtonGetter(4), LoggerRelations.PLACEHOLDER, logger);
		button5 = new LoggerButton(getButtonGetter(5), LoggerRelations.PLACEHOLDER, logger);
	}

	//TODO - make sure axes ports are correct
	public double getAxisX() {
		return getRawAxis(1);
	}

	public double getAxisY() {
		return getRawAxis(2);
	}

	@Override
	public double[] getValues(double[] values) {
		// TODO Auto-generated method stub
		return null;
	}
}