package frc.robot.oi;

import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.Ports;

/**
 * Wrapper class for basic joystick functionality
 */
public class JoystickDriver extends GenericDriver {

	public LoggerButton
	button1,
	button2,
	button3,
	button4,
	button5;

	public LoggerAxis
	axisX,
	axisY;

	public JoystickDriver(Ports port, SyncLogger logger) {
		this.port = port.port;
		this.logger = logger;

		button1 = generateLoggerButton(1, LoggerRelations.PLACEHOLDER);
		button2 = generateLoggerButton(2, LoggerRelations.PLACEHOLDER);
		button3 = generateLoggerButton(3, LoggerRelations.PLACEHOLDER);
		button4 = generateLoggerButton(4, LoggerRelations.PLACEHOLDER);
		button5 = generateLoggerButton(5, LoggerRelations.PLACEHOLDER);

		//TODO - make sure axes ports are correct
		axisX = generateLoggerAxis(1, LoggerRelations.PLACEHOLDER);
		axisY = generateLoggerAxis(2, LoggerRelations.PLACEHOLDER);
	}
}