package frc.robot.oi.drivers;

import frc.robot.oi.inputs.OIAxis;
import frc.robot.oi.inputs.OIButton;

/**
 * Wrapper class for basic joystick functionality
 */
public class JoystickDriver extends GenericDriver {

	public OIButton
	button5,
	button2,
	button3,
	button4,
	trigger;

	public OIAxis
	axisX,
	axisY,
	axisZ;

	public JoystickDriver(int port) {
		super(port);

		//TODO - make sure ports are correct
		trigger = generateOIButton(1);

		button2 = generateOIButton(2);
		button3 = generateOIButton(3);
		button4 = generateOIButton(4);
		button5 = generateOIButton(5);

		axisX = generateOIAxis(0);
		axisY = generateOIAxis(1);
		axisZ = generateOIAxis(2);
	}
}