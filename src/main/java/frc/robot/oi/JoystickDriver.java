package frc.robot.oi;

/**
 * Wrapper class for basic joystick functionality
 */
public class JoystickDriver extends GenericDriver {

	public LoggerButton
	button5,
	button2,
	button3,
	button4,
	trigger;

	public LoggerAxis
	axisX,
	axisY,
	axisZ;

	public JoystickDriver(int port) {
		super(port);

		//TODO - make sure ports are correct
		trigger = generateLoggerButton(1);

		button2 = generateLoggerButton(2);
		button3 = generateLoggerButton(3);
		button4 = generateLoggerButton(4);
		button5 = generateLoggerButton(5);

		axisX = generateLoggerAxis(0);
		axisY = generateLoggerAxis(1);
		axisZ = generateLoggerAxis(2);
	}
}