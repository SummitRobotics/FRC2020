package frc.robot.oi;

import edu.wpi.first.wpilibj.DriverStation;

public abstract class GenericDriver {

	private DriverStation driverStation = DriverStation.getInstance();
	protected int port = 0;

	/**
     * Gets analog axis
     * @param axis the axis number
     * @return the axis value
     */
    protected double getRawAxis(int axis) {
        return driverStation.getStickAxis(port, axis);
    }

    /**
     * Gets digital output
     * @param button the button number
     * @return whether the output is on or off
     */
    protected boolean getRawButton(int button) {
        return driverStation.getStickButton(port, button);
    }

	/**
	 * Gets a getter function for a digital output
	 * @param button the button number
	 * @return the getter function
	 */
	protected Getter getButtonGetter(int button) {
		return () -> getRawButton(button);
	}
}