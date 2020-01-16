 package frc.robot.devices;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.logging.Logger;
import frc.robot.utilities.Constants;

public class Lemonlight implements Logger{

    NetworkTable limelight;

    NetworkTableEntry tv, tx, ty, ta, ledMode, camMode, pipeline;

    public Lemonlight() {
        limelight = NetworkTableInstance.getDefault().getTable("limelight");

        tv = limelight.getEntry("tv");
        tx = limelight.getEntry("tx");
        ty = limelight.getEntry("ty");
        ta = limelight.getEntry("ta");

        ledMode = limelight.getEntry("ledMode");
        camMode = limelight.getEntry("camMode");

        pipeline = limelight.getEntry("pipeline");
    }

    @Override
    public double[] getValues(double[] values) {
        values[Constants.LoggerRelations.LEMONLIGHT_HAS_TARGET.value] = (hasTarget()) ? 1 : 0;
        values[Constants.LoggerRelations.LEMONLIGHT_X_OFF.value] = getHorizontalOffset();
        values[Constants.LoggerRelations.LEMONLIGHT_Y_OFF.value] = getVerticalOffset();     
        return values;
    }

    /**
     * Enum to describe the state of the LED
     */
    public enum LEDModes {
        PIPELINE(0), // controlled through vision pipeline
        FORCE_OFF(1), // turns off LEDs
        FORCE_BLINK(2), // blinks LEDs
        FORCE_ON(3); // turns on LEDs

        public int value;
        private LEDModes(int value) {
            this.value = value;
        }
    }

    /**
     * Enum to describe the state of the limelight camera
     */
    public enum CamModes {
        VISION_PROCESSOR(0), // camera processes data for vision targeting
        DRIVER_CAMERA(1); // camera sends data as a driver feed

        public int value;
        private CamModes(int value) {
            this.value = value;
        }
    }

    /**
     * Sets the limelight LED mode
     * @param mode the new LED mode
     */
    public void setLEDMode(LEDModes mode) {
        ledMode.setDouble(mode.value);
    }

    /**
     * Sets the camera's mode
     * @param mode the new camera mode
     */
    public void setCamMode(CamModes mode) {
        camMode.setDouble(mode.value);
    }

    /**
     * Sets the active camera vision pipeline
     * @param pipe pipeline between 0 and 9
     */
    public void setPipeline(int pipe) {
        pipeline.setDouble(pipe); //sets pip equal to double
    }


    /**
     * Checks if the limelight has aquired a vision target
     */
    public boolean hasTarget() {
        return tv.getDouble(0) == 1;
    }

    /**
     * Gets the horizontal offset from the vision target to the crosshair
     * @return the offset
     */
    public double getHorizontalOffset() {
        return tx.getDouble(0);
    }

    /**
     * Gets the vertical offset from the vision target to the crosshair
     * @return the offset
     */
    public double getVerticalOffset() {
        return ty.getDouble(0);
    }

    /**
     * Gets the area of the target
     * @return the target area
     */
    public double getAreaPercentage() {
        return ta.getDouble(0);
    }
}