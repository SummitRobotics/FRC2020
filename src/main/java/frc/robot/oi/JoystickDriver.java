package frc.robot.oi;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;

/**
 * Wrapper class for basic joystick functionality
 */
public class JoystickDriver extends GenericDriver implements Subsystem {

	public LoggerButton
	button5,
	button2,
	button3,
	button4,
	trigger;

	public LoggerAxis
	axisX,
	axisY;

	public JoystickDriver(int port, SyncLogger logger) {
		super(port, logger);

		//TODO - make sure ports are correct
		trigger = generateLoggerButton(1, LoggerRelations.PLACEHOLDER);

		button2 = generateLoggerButton(2, LoggerRelations.PLACEHOLDER);
		button3 = generateLoggerButton(3, LoggerRelations.PLACEHOLDER);
		button4 = generateLoggerButton(4, LoggerRelations.PLACEHOLDER);
		button5 = generateLoggerButton(5, LoggerRelations.PLACEHOLDER);

		axisX = generateLoggerAxis(0, LoggerRelations.PLACEHOLDER);
		axisY = generateLoggerAxis(1, LoggerRelations.PLACEHOLDER);
	}
}